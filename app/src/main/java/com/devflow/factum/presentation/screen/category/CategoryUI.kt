package com.devflow.factum.presentation.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.devflow.factum.R
import com.devflow.factum.presentation.component.FactCardItem
import com.devflow.factum.presentation.component.topbar.AppBarActionItem
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarActions
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarHeadline
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.util.VisualContent
import com.devflow.factum.util.scrollEndCallback

@Composable
fun CategoryUI(
    state: CategoryUIState,
    onAction: (CategoryUIAction) -> Unit,
    category: String
) {
    val localIndex = stringArrayResource(R.array.category_items).indexOf(category)
    val factIcon = VisualContent.getIcons()[localIndex]
    val factColor = VisualContent.getCardColors()[localIndex]
    val categoryOriginalName = stringArrayResource(R.array.categories)[localIndex]

    val lazyGridState = rememberLazyGridState()

    ProvideAppBarHeadline(categoryOriginalName)
    ProvideAppBarActions(
        list = listOf(
            AppBarActionItem(
                iconId = if (state.isFavorite) R.drawable.ic_round_like_24
                else R.drawable.ic_outline_like_24,
                color = MaterialTheme.colorScheme.primary,
                action = { onAction(CategoryUIAction.OnFavoriteAction()) }
            )
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(Size.GridCellsAdaptive),
        modifier = Modifier.fillMaxSize(),
        state = lazyGridState.also {
            it.scrollEndCallback {
                onAction(CategoryUIAction.OnLoadFact())
            }
        },
        contentPadding = PaddingValues(Padding.Standard),
        verticalArrangement = Arrangement.spacedBy(Padding.Standard),
        horizontalArrangement = Arrangement.spacedBy(Padding.Standard)
    ) {
        itemsIndexed(state.list) { index, fact ->
            FactCardItem(
                fact = fact,
                icon = factIcon,
                color = factColor,
                onClickAction = {
                    onAction(CategoryUIAction.OnFactClickAction(index, localIndex))
                }
            )
        }
    }
}