package com.devflow.factum.domain.usecase

import com.devflow.factum.R
import com.devflow.factum.domain.model.Time
import com.devflow.factum.domain.repository.SharedPrefRepository
import com.devflow.factum.util.ResourceManager
import javax.inject.Inject

class WriteNotificationUseCase @Inject constructor(
    private val repository: SharedPrefRepository,
    private val resourceManager: ResourceManager
) {
    suspend fun execute(time: Time) {
        val name = resourceManager.getString(R.string.shared_pref_main_notifications)
        val data = repository
            .read(name, emptySet<String>())
            .plus(time.toString())

        repository.write(
            name = name,
            data = data
        )
    }
}