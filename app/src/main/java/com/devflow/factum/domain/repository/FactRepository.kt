package com.devflow.factum.domain.repository

import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.Fact
import kotlinx.coroutines.flow.Flow

interface FactRepository {

    suspend fun insert(fact: Fact): Result<Unit, DataError.Local>

    suspend fun delete(fact: Fact): Result<Unit, DataError.Local>

    fun getAll(): Flow<List<Fact>>

    suspend fun isFactExists(
        category: String,
        documentId: String
    ): Boolean
}