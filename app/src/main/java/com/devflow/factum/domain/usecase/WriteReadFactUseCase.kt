package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.FactBase
import com.devflow.factum.domain.model.ReadFact
import com.devflow.factum.domain.repository.ReadFactRepository
import javax.inject.Inject

class WriteReadFactUseCase @Inject constructor(
    private val readFactRepository: ReadFactRepository
) {
    suspend fun execute(factBase: FactBase): Result<Unit, DataError.Local> =
        readFactRepository.insert(ReadFact(factBase))
}