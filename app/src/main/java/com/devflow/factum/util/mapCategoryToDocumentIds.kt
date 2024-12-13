package com.devflow.factum.util

import com.devflow.factum.domain.model.FactBase

fun mapCategoryToDocumentIds(list: List<FactBase>): Map<String, List<String>> {
    return list.groupBy(
        keySelector = { it.category },
        valueTransform = { it.documentId }
    )
}