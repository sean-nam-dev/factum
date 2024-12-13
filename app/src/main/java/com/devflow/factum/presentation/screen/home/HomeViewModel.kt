package com.devflow.factum.presentation.screen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.R
import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase
import com.devflow.factum.domain.usecase.ContactThroughMailUseCase
import com.devflow.factum.domain.usecase.DeleteAllByCategoryReadFactUseCase
import com.devflow.factum.domain.usecase.DeleteFactUseCase
import com.devflow.factum.domain.usecase.GetCollectionSizeUseCase
import com.devflow.factum.domain.usecase.GetFactFromServerUseCase
import com.devflow.factum.domain.usecase.GetReadFactUseCase
import com.devflow.factum.domain.usecase.IsFactExistsUseCase
import com.devflow.factum.domain.usecase.RandomizerUseCase
import com.devflow.factum.domain.usecase.ReadCategoryUseCase
import com.devflow.factum.domain.usecase.WriteFactUseCase
import com.devflow.factum.domain.usecase.WriteReadFactUseCase
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import com.devflow.factum.util.ContextResourceManager
import com.devflow.factum.util.Temp
import com.devflow.factum.domain.core.Result
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val getFactFromServerUseCase: GetFactFromServerUseCase,
    private val readCategoryUseCase: ReadCategoryUseCase,
    private val getReadFactUseCase: GetReadFactUseCase,
    private val writeReadFactUseCase: WriteReadFactUseCase,
    private val randomizerUseCase: RandomizerUseCase,
    private val getCollectionSizeUseCase: GetCollectionSizeUseCase,
    private val deleteAllByCategoryReadFactUseCase: DeleteAllByCategoryReadFactUseCase,
    private val contactThroughMailUseCase: ContactThroughMailUseCase,
    private val isFactExistsUseCase: IsFactExistsUseCase,
    private val writeFactUseCase: WriteFactUseCase,
    private val deleteFactUseCase: DeleteFactUseCase,
    private val resourceManager: ContextResourceManager
): ViewModel() {
    private val _state = MutableStateFlow(HomeUIState(Temp.getFactList()))
    val state = _state
        .onStart { loadFact() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeUIState()
        )

    private val _errorCounter = MutableStateFlow(0)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onHomeUIAction(action: HomeUIAction) {
        when(action) {
            is HomeUIAction.OnFactClickAction -> {
                navigateToItem(
                    index = action.index,
                    localIndex = action.localIndex
                )
                registerItem(action.index)
            }
            is HomeUIAction.OnLoadFact -> {
                loadFact()
            }
            is HomeUIAction.OnReportAction -> report(
                context = action.context,
                factBase = action.factBase
            )
            is HomeUIAction.OnFavoriteAction -> {
                favorite(action.fact)
            }
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

    private fun favorite(fact: Fact) {
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

    private fun loadFact() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val categorySet = readCategoryUseCase.execute()

            if (categorySet.isEmpty()) {
                withContext(Dispatchers.Main.immediate) {
                    _toastMessage.emit("Empty")
                }
            } else {
                val readFactMap = getReadFactUseCase.execute(categorySet)

                repeat(20) {
                    fetchFact(
                        categorySet = categorySet,
                        readFactMap = readFactMap,
                        onSuccess = { fact ->
                            _state.update { it.copy(list = it.list + fact) }
                        },
                        onError = {
                            _errorCounter.update { it + 1 }
                        }
                    )
                }

                retryFailedFacts(
                    categorySet = categorySet,
                    readFactMap = readFactMap
                )
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun retryFailedFacts(
        categorySet: Set<String>,
        readFactMap: Map<String, List<String>>
    ) {
        while (_errorCounter.value > 0) {
            fetchFact(
                categorySet = categorySet,
                readFactMap = readFactMap,
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

    private suspend fun fetchFact(
        categorySet: Set<String>,
        readFactMap: Map<String, List<String>>,
        onSuccess: (Fact) -> Unit,
        onError: (DataError.Network) -> Unit
    ) {
        val randomCategory = categorySet.random()
        val currentReadFactValues = readFactMap[randomCategory].orEmpty()

        val existingNumberList = getIdListByCategory(randomCategory) + currentReadFactValues
        val randomId = randomizerUseCase.getRandomUniqueNumber(existingNumberList)

        when (val factOrError = getFactFromServerUseCase.execute(randomCategory, randomId)) {
            is Result.Success -> onSuccess(factOrError.data)
            is Result.Error -> onError(factOrError.error)
        }
    }

    private fun navigateToItem(
        index: Int,
        localIndex: Int
    ) {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.DetailScreen(
                    index = index,
                    localIndex = localIndex
                )
            )

            writeReadFactToCache(index)
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

    private suspend fun getIdListByCategory(category: String): List<String> {
        return withContext(Dispatchers.Default) {
            _state.value.list
                .filter { it.factBase.category == category }
                .map { it.factBase.documentId }
        }
    }

    private suspend fun writeReadFactToCache(index: Int) {
        val factBase = _state.value.list[index].factBase

        when(writeReadFactUseCase.execute(factBase)) {
            is Result.Success -> {
                when(val getCollectionSizeResult = getCollectionSizeUseCase.execute(factBase.category)) {
                    is Result.Success -> {
                        val readFactSize = getReadFactUseCase.execute(setOf(factBase.category)).values.size.toLong()

                        if (readFactSize == getCollectionSizeResult.data) {
                            deleteAllByCategoryReadFactUseCase.execute(factBase.category)
                        }
                    }
                    is Result.Error -> {}
                }
            }
            is Result.Error -> {}
        }
    }
}