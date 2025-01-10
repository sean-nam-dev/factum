package com.devflow.factum.presentation.screen.deeplink

import android.content.Context

sealed class DeeplinkUIAction {
    data object OnFavoriteItemAction: DeeplinkUIAction()
    data class OnReportAction(val context: Context): DeeplinkUIAction()
}