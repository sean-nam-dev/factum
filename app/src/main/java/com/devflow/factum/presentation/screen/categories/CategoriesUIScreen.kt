package com.devflow.factum.presentation.screen.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.factum.R
import com.devflow.factum.util.VisualContent

@Composable
fun CategoriesUIScreen(
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    CategoriesUI(
        icons = VisualContent.getIcons(),
        titles = stringArrayResource(R.array.categories),
        labels = stringArrayResource(R.array.category_items),
        onAction = viewModel::onCategoriesUIAction
    )
}