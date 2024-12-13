package com.devflow.factum.presentation.screen.start

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.start.StartViewModel

@Composable
fun StartUIScreen(
    viewModel: StartViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    StartUI(
        state = state.value,
        onAction = viewModel::onStartUIAction
    )
}