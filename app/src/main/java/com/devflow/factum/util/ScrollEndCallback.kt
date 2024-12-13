package com.devflow.factum.util

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@SuppressLint("ComposableNaming")
@OptIn(FlowPreview::class)
@Composable
inline fun LazyGridState.scrollEndCallback(
    crossinline callback: () -> Unit
) {
    LaunchedEffect(this) {
        snapshotFlow { layoutInfo }
            .filter { it. totalItemsCount > 0}
            .map { it.visibleItemsInfo.lastOrNull()?.index == it.totalItemsCount - 2 }
            .distinctUntilChanged { old, new -> old && new }
            .debounce(300L)
            .filter { it }
            .collectLatest { callback() }
    }
}