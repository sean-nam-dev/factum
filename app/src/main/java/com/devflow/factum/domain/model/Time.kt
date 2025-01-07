package com.devflow.factum.domain.model

data class Time(
    val hour: Int,
    val minute: Int,
    val isActive: Boolean = false
) {
    override fun toString(): String = "${hour}:${minute}:${isActive}"

    override fun equals(other: Any?): Boolean {
        return if (other is Time) {
            this.hashCode() == other.hashCode()
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minute
        result = 31 * result + isActive.hashCode()
        return result
    }
}