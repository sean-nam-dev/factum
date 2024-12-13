package com.devflow.factum.presentation.screen.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devflow.factum.presentation.component.CategoryItem
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size

@Composable
fun CategoriesUI(
    icons: List<Int>,
    titles: Array<String>,
    labels: Array<String>,
    onAction: (CategoriesUIAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(Size.ItemSize),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Padding.Standard),
        verticalArrangement = Arrangement.spacedBy(Padding.Standard),
        horizontalArrangement = Arrangement.spacedBy(Padding.Standard)
    ) {
        itemsIndexed(titles) { index, title ->
            CategoryItem(
                title = title,
                iconId = icons[index],
                onClickAction = {
                    onAction(CategoriesUIAction.OnCategoryClickAction(labels[index]))
                }
            )
        }
    }
}
