package com.devflow.factum.domain.repository

import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.Fact

interface FirestoreRepository {

    suspend fun getFactFromServer(
        collectionName: String,
        documentId: String,
    ): Result<Fact, DataError.Network>

    suspend fun getFactListFromCache(
        collectionName: String
    ): Result<List<Fact>, DataError.Local>

    suspend fun getCollectionSize(
        collectionName: String
    ): Result<Long, DataError.Network>
}