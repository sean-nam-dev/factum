package com.devflow.factum.presentation.screen.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.home.HomeViewModel

@Composable
fun HomeUIScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    HomeUI(
        state = state.value,
        onAction = viewModel::onHomeUIAction
    )
}