package com.devflow.factum.presentation.screen.settings

import android.content.Context

sealed class SettingsUIAction {
    data class OnCategoryStateChange(val category: String): SettingsUIAction()
    data class OnPersonalizationItemClick(val index: Int): SettingsUIAction()
    data class OnOtherItemClick(
        val index: Int,
        val context: Context,
    ): SettingsUIAction()
}