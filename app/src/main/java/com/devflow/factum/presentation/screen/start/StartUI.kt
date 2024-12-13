package com.devflow.factum.presentation.screen.start

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.devflow.factum.R
import com.devflow.factum.presentation.component.CategoryPickerItem
import com.devflow.factum.ui.theme.FactumTheme
import com.devflow.factum.util.Padding
import com.devflow.factum.util.Size
import com.devflow.factum.util.VisualContent

@Composable
fun StartUI(
    state: StartUIState,
    onAction: (StartUIAction) -> Unit
) {
    val imageList = VisualContent.getImageList()
    val categoryList = stringArrayResource(R.array.categories)
    val categoryItemList = stringArrayResource(R.array.category_items)
    val minSize = minOf(imageList.size, categoryList.size)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(Size.GridCellsAdaptive),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Padding.Large),
        horizontalArrangement = Arrangement.spacedBy(Padding.Large)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = stringResource(id = R.string.choose_favorite_categories),
                modifier = Modifier.padding(bottom = Padding.Large),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        items(count = minSize) { index ->
            CategoryPickerItem(
                imageId = imageList[index],
                title = categoryList[index],
                isChecked = state.categorySet.contains(categoryItemList[index]),
                onCheckedChange = {
                    onAction(StartUIAction.OnCategoryStateChange(categoryItemList[index]))
                }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            val color = if (state.isDoneButtonVisible)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

            OutlinedButton(
                onClick = {
                    onAction(StartUIAction.OnDoneClickAction())
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = Padding.Standard),
                enabled = state.isDoneButtonVisible,
                border = BorderStroke(
                    width = Size.OutlinedButtonBorderWidth,
                    color = color
                )
            ) {
                Text(
                    text = stringResource(id = R.string.ready),
                    style = MaterialTheme.typography
                        .labelLarge
                        .copy(color = color),
                )
            }
        }
    }
}

@Preview
@Composable
private fun StartUIPreview() {
    FactumTheme {
        StartUI(
            StartUIState(
                isDoneButtonVisible = true
            )
        ) { }
    }
}