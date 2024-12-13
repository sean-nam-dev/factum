package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetFactFromServerUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend fun execute(
        collectionName: String,
        documentId: String
    ) = firestoreRepository.getFactFromServer(collectionName, documentId)
}