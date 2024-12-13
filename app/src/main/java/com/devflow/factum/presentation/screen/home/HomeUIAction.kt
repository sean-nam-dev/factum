package com.devflow.factum.presentation.screen.home

import android.content.Context
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase

sealed class HomeUIAction {
    data class OnFactClickAction(
        val index: Int,
        val localIndex: Int,
    ): HomeUIAction()
    data class OnLoadFact(val action: Unit = Unit): HomeUIAction()
    data class OnReportAction(
        val context: Context,
        val factBase: FactBase
    ): HomeUIAction()
    data class OnFavoriteAction(val fact: Fact): HomeUIAction()
}