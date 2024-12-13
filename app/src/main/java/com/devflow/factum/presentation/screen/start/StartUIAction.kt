package com.devflow.factum.presentation.screen.start

sealed class StartUIAction {
    data class OnCategoryStateChange(val itemCategory: String): StartUIAction()
    data class OnDoneClickAction(val action: Unit = Unit): StartUIAction()
}