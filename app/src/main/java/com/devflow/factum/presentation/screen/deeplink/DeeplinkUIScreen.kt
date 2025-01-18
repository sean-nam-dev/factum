package com.devflow.factum.presentation.screen.deeplink

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devflow.factum.R
import com.devflow.factum.presentation.screen.detail.DetailUI
import com.devflow.factum.util.VisualContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DeeplinkUIScreen(
    viewModel: DeeplinkViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.toastMessage) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    val localIndex = stringArrayResource(R.array.category_items).indexOf(viewModel.category)

    if (state.value.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        DetailUI(
            fact = state.value.fact,
            color = VisualContent.getCardColors()[localIndex],
            isFavorite = state.value.isFavorite,
            toastTrigger = viewModel.toastMessage,
            reportAction = {
                viewModel.onDeeplinkUIAction(
                    DeeplinkUIAction.OnReportAction(context)
                )
            },
            favoriteAction = {
                viewModel.onDeeplinkUIAction(
                    DeeplinkUIAction.OnFavoriteItemAction
                )
            }
        )
    }
}