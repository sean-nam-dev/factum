package com.devflow.factum.presentation.screen.deeplink

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.R
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.usecase.ContactThroughMailUseCase
import com.devflow.factum.domain.usecase.DeleteFactUseCase
import com.devflow.factum.domain.usecase.GetFactFromServerUseCase
import com.devflow.factum.domain.usecase.IsFactExistsUseCase
import com.devflow.factum.domain.usecase.WriteFactUseCase
import com.devflow.factum.util.ContextResourceManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DeeplinkViewModel.DeeplinkViewModelFactory::class)
class DeeplinkViewModel @AssistedInject constructor(
    @Assisted("id") val id: String,
    @Assisted("category") val category: String,
    private val getFactFromServerUseCase: GetFactFromServerUseCase,
    private val isFactExistsUseCase: IsFactExistsUseCase,
    private val deleteFactUseCase: DeleteFactUseCase,
    private val writeFactUseCase: WriteFactUseCase,
    private val contactThroughMailUseCase: ContactThroughMailUseCase,
    private val resourceManager: ContextResourceManager
): ViewModel() {
    private val _state = MutableStateFlow(DeeplinkUIState())
    val state = _state
        .onStart { loadFact() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DeeplinkUIState()
        )

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onDeeplinkUIAction(action: DeeplinkUIAction) {
        when(action) {
            is DeeplinkUIAction.OnFavoriteItemAction -> favorite()
            is DeeplinkUIAction.OnReportAction -> report(action.context)
        }
    }

    private fun loadFact() {
        viewModelScope.launch {
            while (_state.value.isLoading) {
                when(val result = getFactFromServerUseCase.execute(collectionName = category, documentId = id)) {
                    is Result.Error -> {
                        delay(1000L)
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                fact = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun favorite() {
        viewModelScope.launch {
            if (isFactExistsUseCase.execute(_state.value.fact.factBase.category, _state.value.fact.factBase.documentId)) {
                deleteFactUseCase.execute(_state.value.fact)
            } else {
                when(writeFactUseCase.execute(_state.value.fact)) {
                    is Result.Success -> {}
                    is Result.Error -> {}
                }
            }
            _state.update { it.copy(isFavorite = !it.isFavorite) }
        }
    }

    private fun report(context: Context) {
        viewModelScope.launch {
            contactThroughMailUseCase.execute(
                context = context,
                subject = _state.value.fact.factBase.toString(),
                onFailAction = {
                    _toastMessage.emit(resourceManager.getString(R.string.no_app_to_send_email))
                }
            )
        }
    }

    @AssistedFactory
    interface DeeplinkViewModelFactory {
        fun create(
            @Assisted("id") id: String,
            @Assisted("category") category: String
        ): DeeplinkViewModel
    }
}