package com.devflow.factum.domain.repository

import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.ReadFact

interface ReadFactRepository {

    suspend fun insert(readFact: ReadFact): Result<Unit, DataError.Local>

    suspend fun deleteAllByCategory(category: String)

    suspend fun getAllByCategories(categories: Set<String>): List<ReadFact>
}