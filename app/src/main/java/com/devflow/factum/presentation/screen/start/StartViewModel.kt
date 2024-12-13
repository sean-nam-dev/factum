package com.devflow.factum.presentation.screen.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.factum.domain.usecase.WriteCategoryUseCase
import com.devflow.factum.domain.usecase.WriteFirstLaunchUseCase
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val navigator: Navigator,
    private val writeCategoryUseCase: WriteCategoryUseCase,
    private val writeFirstLaunchUseCase: WriteFirstLaunchUseCase
): ViewModel() {
    private val _state = MutableStateFlow(StartUIState())
    val state = _state.asStateFlow()

    fun onStartUIAction(action: StartUIAction) {
        when(action) {
            is StartUIAction.OnCategoryStateChange -> updateCategorySelection(action.itemCategory)
            is StartUIAction.OnDoneClickAction -> doneAction()
        }
    }

    private fun updateCategorySelection(category: String) {
        val set = state.value.categorySet.toMutableSet()
            .apply { if (contains(category)) remove(category) else add(category) }

        _state.update {
            it.copy(
                categorySet = set,
                isDoneButtonVisible = set.isNotEmpty()
            )
        }
    }

    private fun doneAction() {
        viewModelScope.launch {
            writeCategoryUseCase.execute(state.value.categorySet)
            writeFirstLaunchUseCase.execute()

            navigator.navigate(
                destination = Destination.HomeGraph,
                navOptions = {
                    popUpTo(Destination.StartGraph) {
                        inclusive = true
                    }
                }
            )
        }
    }
}