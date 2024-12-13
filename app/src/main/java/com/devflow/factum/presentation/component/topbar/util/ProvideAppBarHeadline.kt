package com.devflow.factum.presentation.component.topbar.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.devflow.factum.presentation.component.topbar.TopAppBarViewModel

@Composable
fun ProvideAppBarHeadline(headline: String) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopAppBarViewModel() }
        )

        LaunchedEffect(headline) {
            viewModel.headline = headline
        }
    }
}