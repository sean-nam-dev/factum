package com.devflow.factum.presentation.screen.categories

sealed class CategoriesUIAction {
    data class OnCategoryClickAction(val documentName: String): CategoriesUIAction()
}
