package com.devflow.factum.presentation.screen.category_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.category.CategoryUIAction
import com.devflow.factum.presentation.screen.category.CategoryViewModel
import com.devflow.factum.presentation.screen.detail.DetailUI
import com.devflow.factum.util.VisualContent

@Composable
fun CategoryDetailUIScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
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
            viewModel.onCategoryUIAction(
                CategoryUIAction.OnReportAction(
                    context = context,
                    factBase = fact.factBase
                )
            )
        },
        favoriteAction = {
            viewModel.onCategoryUIAction(
                CategoryUIAction.OnFavoriteItemAction(
                    fact = fact
                )
            )
        }
    )
}