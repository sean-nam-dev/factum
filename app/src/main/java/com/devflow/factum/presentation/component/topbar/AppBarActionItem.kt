package com.devflow.factum.presentation.component.topbar

import androidx.compose.ui.graphics.Color

data class AppBarActionItem(
    val iconId: Int,
    val color: Color,
    val action: () -> Unit
)