package com.devflow.factum.domain.model

data class FactBase(
    val category: String = "",
    val documentId: String = ""
) {
    override fun toString(): String {
        return "$category #$documentId"
    }
}