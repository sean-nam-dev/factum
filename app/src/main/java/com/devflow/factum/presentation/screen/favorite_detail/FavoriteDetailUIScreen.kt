package com.devflow.factum.presentation.screen.favorite_detail

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.presentation.screen.favorite.FavoriteViewModel
import com.devflow.factum.util.VisualContent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDetailUIScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    index: Int,
    localIndex: Int
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val fact = viewModel.facts.collectAsStateWithLifecycle().value[index]

    LaunchedEffect(viewModel.toastMessage) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

    FavoriteDetailUI(
        fact = fact,
        color = VisualContent.getCardColors()[localIndex],
        scrollBehavior = scrollBehavior,
        onAction = viewModel::onFavoriteUIAction
    )
}