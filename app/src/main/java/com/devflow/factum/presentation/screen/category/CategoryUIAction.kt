package com.devflow.factum.presentation.screen.category

import android.content.Context
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase

sealed class CategoryUIAction {
    data object OnLoadFact: CategoryUIAction()
    data object OnFavoriteAction: CategoryUIAction()
    data class OnFactClickAction(
        val index: Int,
        val localIndex: Int
    ): CategoryUIAction()
    data class OnFavoriteItemAction(val fact: Fact): CategoryUIAction()
    data class OnReportAction(
        val context: Context,
        val factBase: FactBase
    ): CategoryUIAction()
}