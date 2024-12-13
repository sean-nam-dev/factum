package com.devflow.factum.domain.model

data class Category(
    val imageId: Int,
    val title: String,
    val isChecked: Boolean = false
)