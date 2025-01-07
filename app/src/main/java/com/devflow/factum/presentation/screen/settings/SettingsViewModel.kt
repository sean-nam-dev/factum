package com.devflow.factum.presentation.screen.settings

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.R
import com.devflow.factum.domain.model.Time
import com.devflow.factum.domain.usecase.CancelNotificationManagerUseCase
import com.devflow.factum.domain.usecase.ChangeNotificationActivenessUseCase
import com.devflow.factum.domain.usecase.ContactThroughMailUseCase
import com.devflow.factum.domain.usecase.DeleteNotificationUseCase
import com.devflow.factum.domain.usecase.RateAppUseCase
import com.devflow.factum.domain.usecase.ReadCategoryUseCase
import com.devflow.factum.domain.usecase.ReadNotificationUseCase
import com.devflow.factum.domain.usecase.ScheduleNotificationManagerUseCase
import com.devflow.factum.domain.usecase.WriteCategoryUseCase
import com.devflow.factum.domain.usecase.WriteNotificationUseCase
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
    private val appContext: Application,
    private val rateAppUseCase: RateAppUseCase,
    private val readCategoryUseCase: ReadCategoryUseCase,
    private val writeCategoryUseCase: WriteCategoryUseCase,
    private val contactThroughMailUseCase: ContactThroughMailUseCase,
    private val scheduleNotificationManagerUseCase: ScheduleNotificationManagerUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val writeNotificationUseCase: WriteNotificationUseCase,
    private val changeNotificationActivenessUseCase: ChangeNotificationActivenessUseCase,
    private val cancelNotificationManagerUseCase: CancelNotificationManagerUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SettingsUIState())
    val state = _state
        .onStart {
            preLoadData()
            checkPermission()
        }
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
            is SettingsUIAction.OnCheckPermission -> checkPermission()
            is SettingsUIAction.OnAddNewTimeSlot -> addNewTimeSlot()
            is SettingsUIAction.OnTimeDialogConfirmationRequest -> timeDialogConfirmationRequest(action.time)
            is SettingsUIAction.OnTimeDialogDismissRequest -> timeDialogDismissRequest()
            is SettingsUIAction.OnTimeItemDeletionRequest -> timeItemDeletionRequest(action.time)
            is SettingsUIAction.OnTimeItemCheckedStateChange -> timeItemCheckedStateChange(action.time)
            is SettingsUIAction.OnConfirmationDialogDismissRequest -> confirmationDialogDismissRequest()
            is SettingsUIAction.OnConfirmationDialogConfirmationRequest -> confirmationDialogConfirmationRequest()
        }
    }

    private fun confirmationDialogConfirmationRequest() {
        viewModelScope.launch {
            cancelNotificationManagerUseCase.execute(state.value.pendingToDeleteTime!!)
            deleteNotificationUseCase.execute(state.value.pendingToDeleteTime!!)

            _state.update {
                it.copy(
                    timeSet = it.timeSet.minus(it.pendingToDeleteTime!!),
                    isConfirmationDialogShown = false,
                    pendingToDeleteTime = null
                )
            }
        }
    }

    private fun confirmationDialogDismissRequest() {
        _state.update {
            it.copy(
                isConfirmationDialogShown = false,
                pendingToDeleteTime = null
            )
        }
    }

    private fun timeItemCheckedStateChange(time: Time) {
        viewModelScope.launch {
            val newStateValue = !time.isActive
            val updatedSet = _state.value.timeSet
                .map { if (it == time) it.copy(isActive = newStateValue) else it }
                .toSet()

            _state.update { it.copy(timeSet = updatedSet) }

            changeNotificationActivenessUseCase.execute(time)

            if (newStateValue) {
                scheduleNotificationManagerUseCase.execute(time)
            } else {
                cancelNotificationManagerUseCase.execute(time)
            }
        }
    }

    private fun timeItemDeletionRequest(time: Time) {
        _state.update {
            it.copy(
                isConfirmationDialogShown = true,
                pendingToDeleteTime = time
            )
        }
    }

    private fun timeDialogDismissRequest() {
        _state.update { it.copy(isTimeDialogShown = false) }
    }

    private fun timeDialogConfirmationRequest(time: Time) {
        viewModelScope.launch {
            writeNotificationUseCase.execute(time)
            scheduleNotificationManagerUseCase.execute(time)

            _state.update {
                it.copy(
                    timeSet = it.timeSet.plus(time),
                    isTimeDialogShown = false
                )
            }
        }
    }

    private fun addNewTimeSlot() {
        _state.update { it.copy(isTimeDialogShown = true) }
    }

    private suspend fun preLoadData() {
        _state.update {
            it.copy(
                categorySet = readCategoryUseCase.execute(),
                timeSet = readNotificationUseCase.execute()
            )
        }
    }

    private fun checkPermission() {
        _state.update {
            it.copy(
                hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                } else true
            )
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