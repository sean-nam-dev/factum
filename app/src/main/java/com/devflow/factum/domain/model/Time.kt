package com.devflow.factum.domain.model

data class Time(
    val hour: Int,
    val minute: Int,
    val isActive: Boolean = true
) {
    override fun toString(): String = "${hour}:${minute}:${isActive}"
}