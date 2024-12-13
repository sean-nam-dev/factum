package com.devflow.factum.domain.usecase

import android.app.Application
import com.devflow.factum.R
import com.devflow.factum.domain.repository.SharedPrefRepository
import javax.inject.Inject

class WriteFirstLaunchUseCase @Inject constructor(
    private val appContext: Application,
    private val repository: SharedPrefRepository
) {
    suspend fun execute(data: Boolean? = null) {
        val name = appContext.getString(R.string.shared_pref_main_launch)

        repository.write(
            name = name,
            data = data ?: !repository.read(name, true)
        )
    }
}