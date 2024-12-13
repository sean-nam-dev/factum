package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.ReadFactRepository
import javax.inject.Inject

class DeleteAllByCategoryReadFactUseCase @Inject constructor(
    private val readFactRepository: ReadFactRepository
){
    suspend fun execute(category: String) = readFactRepository.deleteAllByCategory(category)
}