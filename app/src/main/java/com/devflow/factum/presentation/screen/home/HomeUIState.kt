package com.devflow.factum.presentation.screen.home

import com.devflow.factum.domain.model.Fact

data class HomeUIState(
    val list: List<Fact> = emptyList(),
    val isLoading: Boolean = true,
    val isFactExists: Boolean = false
)