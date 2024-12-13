package com.devflow.factum.presentation.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
class TopAppBarViewModel: ViewModel() {
    var scrollBehavior by mutableStateOf<TopAppBarScrollBehavior?>(null, referentialEqualityPolicy())
    var headline by mutableStateOf("", referentialEqualityPolicy())
    var actions by mutableStateOf<List<AppBarActionItem>>(emptyList(), referentialEqualityPolicy())
    var color by mutableStateOf<Color?>(null, referentialEqualityPolicy())
}