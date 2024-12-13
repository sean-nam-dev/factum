package com.devflow.factum.presentation.screen.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.R
import com.devflow.factum.domain.usecase.ContactThroughMailUseCase
import com.devflow.factum.domain.usecase.RateAppUseCase
import com.devflow.factum.domain.usecase.ReadCategoryUseCase
import com.devflow.factum.domain.usecase.WriteCategoryUseCase
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import com.devflow.factum.util.ContextResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val resourceManager: ContextResourceManager,
    private val rateAppUseCase: RateAppUseCase,
    private val readCategoryUseCase: ReadCategoryUseCase,
    private val writeCategoryUseCase: WriteCategoryUseCase,
    private val contactThroughMailUseCase: ContactThroughMailUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(SettingsUIState())
    val state = _state
        .onStart { loadCategories() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SettingsUIState()
        )

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onSettingsUIAction(action: SettingsUIAction) {
        when(action) {
            is SettingsUIAction.OnPersonalizationItemClick -> {
                when(action.index) {
                    0 -> navigateTo(Destination.NotificationScreen)
                    1 -> navigateTo(Destination.FavoriteCategoriesScreen)
                }
            }
            is SettingsUIAction.OnOtherItemClick -> {
                when(action.index) {
                    0 -> contactWithUse(action.context)
                    1 -> rateApp(action.context)
                }
            }
            is SettingsUIAction.OnCategoryStateChange -> categoryStateChange(action.category)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(categorySet = readCategoryUseCase.execute()) }
        }
    }

    private fun categoryStateChange(category: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    categorySet = if (it.categorySet.contains(category)) it.categorySet.minus(category)
                    else it.categorySet.plus(category)
                )
            }

            writeCategoryUseCase.execute(_state.value.categorySet)
        }
    }

    private fun navigateTo(destination: Destination) {
        viewModelScope.launch {
            navigator.navigate(destination)
        }
    }

    private fun contactWithUse(context: Context) {
        viewModelScope.launch {
            contactThroughMailUseCase.execute(
                context = context,
                subject = "",
                onFailAction = {
                    _toastMessage.emit(resourceManager.getString(R.string.no_app_to_send_email))
                }
            )
        }
    }

    private fun rateApp(context: Context) {
        rateAppUseCase.execute(context)
    }
}