package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.ReadFactRepository
import com.devflow.factum.util.mapCategoryToDocumentIds
import javax.inject.Inject

class GetReadFactUseCase @Inject constructor(
    private val readFactRepository: ReadFactRepository
) {
    suspend fun execute(categories: Set<String>): Map<String, List<String>> {
        return mapCategoryToDocumentIds(readFactRepository.getAllByCategories(categories).map { it.factBase })
    }
}