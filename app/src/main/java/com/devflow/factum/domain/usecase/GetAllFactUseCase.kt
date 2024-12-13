package com.devflow.factum.domain.usecase

import com.devflow.factum.domain.repository.FactRepository
import javax.inject.Inject

class GetAllFactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    fun execute() = factRepository.getAll()
}