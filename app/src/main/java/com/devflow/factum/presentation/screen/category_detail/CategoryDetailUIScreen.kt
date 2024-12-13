package com.devflow.factum.presentation.screen.category_detail

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.category.CategoryViewModel
import com.devflow.factum.util.VisualContent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailUIScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    index: Int,
    localIndex: Int
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val state = viewModel.state.collectAsStateWithLifecycle()
    val fact = state.value.list[index]
    val isFavorite = state.value.isFactExists

    LaunchedEffect(viewModel.toastMessage) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    CategoryDetailUI(
        fact = fact,
        isFavorite = isFavorite,
        color = VisualContent.getCardColors()[localIndex],
        scrollBehavior = scrollBehavior,
        onAction = viewModel::onCategoryUIAction
    )
}