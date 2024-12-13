package com.devflow.factum.presentation.screen.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.devflow.factum.R
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.presentation.component.FactCardItem
import com.devflow.factum.presentation.screen.favorite.FavoriteUIAction
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.util.VisualContent

@Composable
fun FavoriteUI(
    factList: List<Fact>,
    onAction: (FavoriteUIAction) -> Unit
) {
    val categoryList = stringArrayResource(R.array.category_items)
    val factIconList = VisualContent.getIcons()
    val factColorList = VisualContent.getCardColors()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(Size.GridCellsAdaptive),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Padding.Standard),
        verticalArrangement = Arrangement.spacedBy(Padding.Standard),
        horizontalArrangement = Arrangement.spacedBy(Padding.Standard)
    ) {
        itemsIndexed(factList) { index, fact ->
            val localIndex = categoryList.indexOf(fact.factBase.category)

            FactCardItem(
                fact = fact,
                icon = factIconList[localIndex],
                color = factColorList[localIndex],
                onClickAction = {
                    onAction(FavoriteUIAction.OnFactClickAction(index, localIndex))
                }
            )
        }
    }
}