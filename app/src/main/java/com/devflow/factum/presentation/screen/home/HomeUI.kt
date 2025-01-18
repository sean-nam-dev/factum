package com.devflow.factum.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.devflow.factum.R
import com.devflow.factum.presentation.component.FactCardItem
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.util.VisualContent
import com.devflow.factum.util.scrollEndCallback

@Composable
fun HomeUI(
    state: HomeUIState,
    onAction: (HomeUIAction) -> Unit
) {
    val factIconList = VisualContent.getIcons()
    val factColorList = VisualContent.getCardColors()
    val categoryList = stringArrayResource(R.array.category_items)

    val lazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(Size.GridCellsAdaptive),
        modifier = Modifier.fillMaxSize(),
        state = lazyGridState.also {
            it.scrollEndCallback {
                onAction(HomeUIAction.OnLoadFact)
            }
        },
        contentPadding = PaddingValues(Padding.Standard),
        verticalArrangement = Arrangement.spacedBy(Padding.Standard),
        horizontalArrangement = Arrangement.spacedBy(Padding.Standard)
    ) {
        itemsIndexed(state.list) { index, fact ->
            val localIndex = categoryList.indexOf(fact.factBase.category)

            FactCardItem(
                fact = fact,
                icon = factIconList[localIndex],
                color = factColorList[localIndex],
                onClickAction = {
                    onAction(
                        HomeUIAction.OnFactClickAction(
                            index = index,
                            localIndex = localIndex
                        )
                    )
                }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(Size.CircularProgressIndicator))
                }
            }
        }
    }
}
