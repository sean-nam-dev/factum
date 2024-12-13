package com.devflow.factum.presentation.screen.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.R
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase
import com.devflow.factum.domain.usecase.ContactThroughMailUseCase
import com.devflow.factum.domain.usecase.DeleteFactUseCase
import com.devflow.factum.domain.usecase.GetAllFactUseCase
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import com.devflow.factum.util.ContextResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val navigator: Navigator,
    getAllFactUseCase: GetAllFactUseCase,
    private val deleteFactUseCase: DeleteFactUseCase,
    private val contactThroughMailUseCase: ContactThroughMailUseCase,
    private val resourceManager: ContextResourceManager
): ViewModel() {
    val facts = getAllFactUseCase.execute()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onFavoriteUIAction(action: FavoriteUIAction) {
        when(action) {
            is FavoriteUIAction.OnFactClickAction -> {
                navigateToItem(action.index, action.localIndex)
            }
            is FavoriteUIAction.OnFavoriteItemAction -> favoriteItem(action.fact)
            is FavoriteUIAction.OnReportAction -> report(action.context, action.factBase)
        }
    }

    private fun navigateToItem(
        index: Int,
        localIndex: Int
    ) {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.FavoriteDetailScreen(
                    index = index,
                    localIndex = localIndex
                )
            )
        }
    }

    private fun favoriteItem(fact: Fact) {
        viewModelScope.launch {
            navigator.navigateUp()
            deleteFactUseCase.execute(fact)
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
}