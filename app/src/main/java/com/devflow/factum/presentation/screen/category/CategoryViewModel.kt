package com.devflow.factum.presentation.screen.category

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.R
import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase
import com.devflow.factum.domain.usecase.ContactThroughMailUseCase
import com.devflow.factum.domain.usecase.DeleteFactUseCase
import com.devflow.factum.domain.usecase.GetFactFromServerUseCase
import com.devflow.factum.domain.usecase.IsFactExistsUseCase
import com.devflow.factum.domain.usecase.RandomizerUseCase
import com.devflow.factum.domain.usecase.ReadCategoryUseCase
import com.devflow.factum.domain.usecase.WriteCategoryUseCase
import com.devflow.factum.domain.usecase.WriteFactUseCase
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import com.devflow.factum.util.ContextResourceManager
import com.devflow.factum.domain.core.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = CategoryViewModel.CategoryViewModelFactory::class)
class CategoryViewModel @AssistedInject constructor(
    @Assisted val category: String,
    private val navigator: Navigator,
    private val getFactFromServerUseCase: GetFactFromServerUseCase,
    private val randomizerUseCase: RandomizerUseCase,
    private val readCategoryUseCase: ReadCategoryUseCase,
    private val writeCategoryUseCase: WriteCategoryUseCase,
    private val isFactExistsUseCase: IsFactExistsUseCase,
    private val deleteFactUseCase: DeleteFactUseCase,
    private val writeFactUseCase: WriteFactUseCase,
    private val contactThroughMailUseCase: ContactThroughMailUseCase,
    private val resourceManager: ContextResourceManager
): ViewModel() {
    private val _state = MutableStateFlow(CategoryUIState())
    val state = _state
        .onStart {
            loadFact()
            checkIsFavorite()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CategoryUIState()
        )

    private val _errorCounter = MutableStateFlow(0)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onCategoryUIAction(action: CategoryUIAction) {
        when(action) {
            is CategoryUIAction.OnLoadFact -> loadFact()
            is CategoryUIAction.OnFavoriteAction -> favorite()
            is CategoryUIAction.OnFactClickAction -> {
                navigateToItem(action.index, action.localIndex)
                registerItem(action.index)
            }
            is CategoryUIAction.OnFavoriteItemAction -> favoriteItem(action.fact)
            is CategoryUIAction.OnReportAction -> report(action.context, action.factBase)
        }
    }

    private fun report(
        context: Context,
        factBase: FactBase
    ) {
        viewModelScope.launch {
            contactThroughMailUseCase.execute(
                context = context,
                subject = factBase.toString(),
                onFailAction = {
                    _toastMessage.emit(resourceManager.getString(R.string.no_app_to_send_email))
                }
            )
        }
    }

    private fun favoriteItem(fact: Fact) {
        viewModelScope.launch {
            if (_state.value.isFactExists) {
                deleteFactUseCase.execute(fact)
                _state.update { it.copy(isFactExists = false) }
            } else {
                when(writeFactUseCase.execute(fact)) {
                    is Result.Success -> {
                        _state.update { it.copy(isFactExists = true) }
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun navigateToItem(
        index: Int,
        localIndex: Int
    ) {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.CategoryDetailScreen(
                    index = index,
                    localIndex = localIndex,
                    category = category
                )
            )
        }
    }

    private fun registerItem(index: Int) {
        viewModelScope.launch {
            val factBase = _state.value.list[index].factBase

            _state.update {
                it.copy(
                    isFactExists = isFactExistsUseCase.execute(
                        category = factBase.category,
                        documentId = factBase.documentId
                    )
                )
            }
        }
    }

    private fun loadFact() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            repeat(20) {
                fetchFact(
                    onSuccess = { fact ->
                        _state.update { it.copy(list = it.list + fact) }
                    },
                    onError = {
                        _errorCounter.update { it + 1 }
                    }
                )
            }

            retryFailedFacts()

            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun fetchFact(
        onSuccess: (Fact) -> Unit,
        onError: (DataError.Network) -> Unit
    ) {
        val randomId = randomizerUseCase.getRandomUniqueNumber(getIdList())

        when(val factOrError = getFactFromServerUseCase.execute(category, randomId)) {
            is Result.Success -> onSuccess(factOrError.data)
            is Result.Error -> onError(factOrError.error)
        }
    }

    private suspend fun retryFailedFacts() {
        while (_errorCounter.value > 0) {
            fetchFact(
                onSuccess = { fact ->
                    _state.update { it.copy(list = it.list + fact) }
                    _errorCounter.update { it - 1 }
                },
                onError = { _ ->
                    viewModelScope.launch(Dispatchers.IO) {
                        delay(2000L)
                    }
                }
            )
        }
    }

    private suspend fun getIdList(): List<String> {
        return withContext(Dispatchers.Default) {
            _state.value.list.map { it.factBase.documentId }
        }
    }

    private suspend fun checkIsFavorite() {
        val isFavorite = readCategoryUseCase.execute().contains(category)

        _state.update { it.copy(isFavorite = isFavorite) }
    }

    private fun favorite() {
        viewModelScope.launch {
            val currentCategories = readCategoryUseCase.execute()

            if (_state.value.isFavorite) {
                writeCategoryUseCase
                    .execute(currentCategories.minusElement(category))
            } else {
                writeCategoryUseCase
                    .execute(currentCategories.plus(category))
            }

            _state.update { it.copy(isFavorite = !it.isFavorite) }
        }
    }

    @AssistedFactory
    interface CategoryViewModelFactory {
        fun create(category: String): CategoryViewModel
    }
}