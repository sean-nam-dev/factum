package com.devflow.factum.presentation.screen.settings

import com.devflow.factum.domain.model.Time

data class SettingsUIState(
    val categorySet: Set<String> = emptySet(),
    val timeSet: Set<Time> = emptySet(),
    val isTimeDialogShown: Boolean = false,
    val isConfirmationDialogShown: Boolean = false,
    val hasNotificationPermission: Boolean = true,
    val pendingToDeleteTime: Time? = null
)