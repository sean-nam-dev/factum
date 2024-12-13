package com.devflow.factum.presentation.screen.favorite

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FavoriteUIScreen(
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val factList = viewModel.facts.collectAsStateWithLifecycle()

    FavoriteUI(
        factList = factList.value,
        onAction = viewModel::onFavoriteUIAction
    )
}