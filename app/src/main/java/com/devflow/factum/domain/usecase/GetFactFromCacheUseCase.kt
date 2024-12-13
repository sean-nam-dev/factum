package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetFactListFromCacheUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend fun execute(
        collectionName: String
    ) = firestoreRepository.getFactListFromCache(collectionName)
}