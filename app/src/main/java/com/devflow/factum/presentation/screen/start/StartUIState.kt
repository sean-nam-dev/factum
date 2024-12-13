package com.devflow.factum.presentation.screen.start

data class StartUIState(
    val categorySet: Set<String> = emptySet(),
    val isDoneButtonVisible: Boolean = false
)
