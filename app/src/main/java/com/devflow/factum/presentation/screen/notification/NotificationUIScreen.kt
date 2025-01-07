package com.devflow.factum.presentation.screen.notification

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.settings.SettingsViewModel

@Composable
fun NotificationUIScreen(
    viewModel: SettingsViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    NotificationUI(
        state = state.value,
        onAction = viewModel::onSettingsUIAction
    )
}