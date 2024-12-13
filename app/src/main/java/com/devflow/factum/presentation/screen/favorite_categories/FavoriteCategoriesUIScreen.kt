package com.devflow.factum.presentation.screen.favorite_categories

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.settings.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FavoriteCategoriesUIScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.toastMessage) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    FavoriteCategoriesUI(
        state = state.value,
        onAction = viewModel::onSettingsUIAction
    )
}