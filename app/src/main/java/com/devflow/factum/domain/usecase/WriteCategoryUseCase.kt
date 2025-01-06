package com.devflow.factum.domain.usecase

import android.app.Application
import com.devflow.factum.R
import com.devflow.factum.domain.repository.SharedPrefRepository
import javax.inject.Inject

class WriteCategoryUseCase @Inject constructor(
    private val appContext: Application,
    private val repository: SharedPrefRepository
) {
    suspend fun execute(data: Set<String>) {
        val name = appContext.getString(R.string.shared_pref_main_categories)

        repository.write(
            name = name,
            data = data
        )
    }
}

