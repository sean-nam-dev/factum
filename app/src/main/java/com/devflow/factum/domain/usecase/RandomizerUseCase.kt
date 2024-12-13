package com.devflow.factum.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RandomizerUseCase @Inject constructor(

) {
    suspend fun getRandomUniqueNumber(existingNumberList: List<String>): String {
        var uniqueNumber: String? = null

        withContext(Dispatchers.Default) {
            val formattedList = existingNumberList.mapNotNull { it.toIntOrNull() }
            val availableNumberList = (MIN until MAX).filter { it !in formattedList }

            uniqueNumber = availableNumberList.random().toString()
        }

        return uniqueNumber ?: MIN.toString()
    }

    companion object {
        const val MIN = 0
        const val MAX = 1000
    }
}