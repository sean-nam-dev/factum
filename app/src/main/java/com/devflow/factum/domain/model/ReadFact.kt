package com.devflow.factum.domain.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["category", "documentId"])
data class ReadFact(
    @Embedded
    val factBase: FactBase = FactBase()
)