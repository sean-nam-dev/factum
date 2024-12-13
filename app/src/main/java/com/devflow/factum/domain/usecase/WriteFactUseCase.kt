package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.repository.FactRepository
import javax.inject.Inject

class WriteFactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun execute(fact: Fact): Result<Unit, DataError.Local> {
        return factRepository.insert(fact)
    }
}