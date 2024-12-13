package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.FactRepository
import javax.inject.Inject

class IsFactExistsUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun execute(
        category: String,
        documentId: String
    ): Boolean {
        return factRepository.isFactExists(category, documentId)
    }
}