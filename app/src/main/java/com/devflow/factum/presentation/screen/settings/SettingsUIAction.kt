package com.devflow.factum.presentation.screen.settings

import android.content.Context
import com.devflow.factum.domain.model.Time

sealed class SettingsUIAction {
    data class OnCategoryStateChange(val category: String): SettingsUIAction()
    data class OnPersonalizationItemClick(val index: Int): SettingsUIAction()
    data class OnOtherItemClick(
        val index: Int,
        val context: Context,
    ): SettingsUIAction()
    data class OnScheduleNotification(
        val hour: Int,
        val minute: Int
    ): SettingsUIAction()
    data object OnCheckPermission: SettingsUIAction()
    data object OnAddNewTimeSlot: SettingsUIAction()
    data object OnTimeDialogDismissRequest: SettingsUIAction()
    data class OnTimeDialogConfirmRequest(val time: Time): SettingsUIAction()
}