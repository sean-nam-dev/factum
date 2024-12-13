package com.devflow.factum.presentation.screen.settings

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsUIScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {


    SettingsUI(
        onAction = viewModel::onSettingsUIAction
    )
}