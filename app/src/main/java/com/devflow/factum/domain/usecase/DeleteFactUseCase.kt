package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.repository.FactRepository
import javax.inject.Inject

class DeleteFactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun execute(fact: Fact) {
        factRepository.delete(fact)
    }
}