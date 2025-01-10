package com.devflow.factum.presentation.screen.deeplink

import com.devflow.factum.domain.model.Fact

data class DeeplinkUIState(
    val fact: Fact = Fact(),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true
)