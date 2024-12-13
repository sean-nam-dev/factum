package com.devflow.factum.presentation.screen.category

import com.devflow.factum.domain.model.Fact

data class CategoryUIState(
    val list: List<Fact> = emptyList(),
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val isFactExists: Boolean = false
)