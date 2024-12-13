package com.devflow.factum.presentation.component.topbar.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.devflow.factum.presentation.component.topbar.TopAppBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvideScrollBehavior(scrollBehavior: TopAppBarScrollBehavior) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopAppBarViewModel() }
        )

        LaunchedEffect(scrollBehavior) {
            viewModel.scrollBehavior = scrollBehavior
        }
    }
}