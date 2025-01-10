package com.devflow.factum.presentation.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.home.HomeUIAction
import com.devflow.factum.presentation.screen.home.HomeViewModel
import com.devflow.factum.util.VisualContent

@Composable
fun DetailUIScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    index: Int,
    localIndex: Int
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsStateWithLifecycle()
    val fact = state.value.list[index]
    val isFavorite = state.value.isFactExists

    DetailUI(
        fact = fact,
        color = VisualContent.getCardColors()[localIndex],
        isFavorite = isFavorite,
        toastTrigger = viewModel.toastMessage,
        reportAction = {
            viewModel.onHomeUIAction(
                HomeUIAction.OnReportAction(
                    context = context,
                    factBase = fact.factBase
                )
            )
        },
        favoriteAction = {
            viewModel.onHomeUIAction(
                HomeUIAction.OnFavoriteAction(
                    fact = fact
                )
            )
        }
    )
}
