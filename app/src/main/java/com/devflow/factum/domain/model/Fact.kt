package com.devflow.factum.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import kotlin.math.ceil

@Entity(primaryKeys = ["category", "documentId"])
data class Fact(
    @Embedded
    val factBase: FactBase = FactBase(),

    val title: String = "",
    val text: String = "",
    val readTime: Int = 0
)
