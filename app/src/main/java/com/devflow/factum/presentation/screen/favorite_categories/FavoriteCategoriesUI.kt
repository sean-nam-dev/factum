package com.devflow.factum.presentation.screen.favorite_categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.devflow.factum.R
import com.devflow.factum.presentation.component.CategoryPickerItem
import com.devflow.factum.presentation.component.topbar.util.ProvideAppBarHeadline
import com.devflow.factum.presentation.screen.settings.SettingsUIAction
import com.devflow.factum.presentation.screen.settings.SettingsUIState
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.util.VisualContent

@Composable
fun FavoriteCategoriesUI(
    state: SettingsUIState,
    onAction: (SettingsUIAction) -> Unit
) {
    val imageList = VisualContent.getImageList()
    val categoryList = stringArrayResource(R.array.categories)
    val categoryItemList = stringArrayResource(R.array.category_items)
    val minSize = minOf(imageList.size, categoryList.size)

    ProvideAppBarHeadline(headline = stringResource(R.string.choose_favorite_categories))

    LazyVerticalGrid(
        columns = GridCells.Adaptive(Size.GridCellsAdaptive),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Padding.Large),
        horizontalArrangement = Arrangement.spacedBy(Padding.Large)
    ) {
        items(count = minSize) { index ->
            CategoryPickerItem(
                imageId = imageList[index],
                title = categoryList[index],
                isChecked = state.categorySet.contains(categoryItemList[index]),
                onCheckedChange = {
                    onAction(SettingsUIAction.OnCategoryStateChange(categoryItemList[index]))
                }
            )
        }
    }
}