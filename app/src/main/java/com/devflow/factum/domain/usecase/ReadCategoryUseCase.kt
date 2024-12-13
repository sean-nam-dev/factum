package com.devflow.factum.domain.usecase

import android.app.Application
import com.devflow.factum.R
import com.devflow.factum.domain.repository.SharedPrefRepository
import javax.inject.Inject

class ReadCategoryUseCase @Inject constructor(
    private val appContext: Application,
    private val repository: SharedPrefRepository
) {
    suspend fun execute(): Set<String> {
        val name = appContext.getString(R.string.shared_pref_main_categories)
        val defaultValue = appContext.resources.getStringArray(R.array.category_items).toSet()

        return repository.read(name, defaultValue)
    }
}