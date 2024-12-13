package com.devflow.factum.presentation.screen.favorite

import android.content.Context
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase

sealed class FavoriteUIAction {
    data class OnFactClickAction(
        val index: Int,
        val localIndex: Int
    ): FavoriteUIAction()
    data class OnFavoriteItemAction(val fact: Fact): FavoriteUIAction()
    data class OnReportAction(
        val context: Context,
        val factBase: FactBase
    ): FavoriteUIAction()
}