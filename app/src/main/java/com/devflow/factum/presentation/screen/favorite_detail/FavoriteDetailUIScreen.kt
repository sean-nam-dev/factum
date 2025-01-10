package com.devflow.factum.presentation.screen.favorite_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.detail.DetailUI
import com.devflow.factum.presentation.screen.favorite.FavoriteUIAction
import com.devflow.factum.presentation.screen.favorite.FavoriteViewModel
import com.devflow.factum.util.VisualContent

@Composable
fun FavoriteDetailUIScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    index: Int,
    localIndex: Int
) {
    val context = LocalContext.current

    val fact = viewModel.facts.collectAsStateWithLifecycle().value[index]

    DetailUI(
        fact = fact,
        color = VisualContent.getCardColors()[localIndex],
        isFavorite = true,
        toastTrigger = viewModel.toastMessage,
        reportAction = {
            viewModel.onFavoriteUIAction(
                FavoriteUIAction.OnReportAction(
                    context = context,
                    factBase = fact.factBase
                )
            )
        },
        favoriteAction = {
            viewModel.onFavoriteUIAction(
                FavoriteUIAction.OnFavoriteItemAction(
                    fact = fact
                )
            )
        }
    )
}