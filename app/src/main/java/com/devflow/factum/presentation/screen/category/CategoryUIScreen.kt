package com.devflow.factum.presentation.screen.category

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CategoryUIScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    CategoryUI(
        state = state.value,
        onAction = viewModel::onCategoryUIAction,
        category = viewModel.category
    )
}