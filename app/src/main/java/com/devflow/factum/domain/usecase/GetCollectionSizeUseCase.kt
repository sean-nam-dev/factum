package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetCollectionSizeUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend fun execute(collectionName: String) = firestoreRepository.getCollectionSize(collectionName)
}