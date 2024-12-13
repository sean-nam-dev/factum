package com.devflow.factum.presentation.screen.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val navigator: Navigator

): ViewModel() {
    fun onCategoriesUIAction(action: CategoriesUIAction) {
        when(action) {
            is CategoriesUIAction.OnCategoryClickAction -> {
                viewModelScope.launch {
                    navigator.navigate(
                        destination = Destination.CategoryScreen(
                            documentName = action.documentName
                        )
                    )
                }
            }
        }
    }
}