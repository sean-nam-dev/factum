package com.devflow.factum.presentation.component.snackbar

import androidx.compose.material3.SnackbarDuration

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val duration: SnackbarDuration
)
