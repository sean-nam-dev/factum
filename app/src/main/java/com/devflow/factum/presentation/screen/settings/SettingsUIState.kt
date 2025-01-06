package com.devflow.factum.presentation.screen.settings

import com.devflow.factum.domain.model.Time

data class SettingsUIState(
    val categorySet: Set<String> = emptySet(),
    val timeList: List<Time> = emptyList(),
    val isTimeDialogShown: Boolean = false,
    val isConfirmationDialogShown: Boolean = false,
    val hasNotificationPermission: Boolean = true
)