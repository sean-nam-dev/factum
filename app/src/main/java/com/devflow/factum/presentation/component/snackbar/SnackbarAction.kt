package com.devflow.factum.presentation.component.snackbar

data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit
)
