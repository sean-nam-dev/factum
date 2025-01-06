package com.devflow.factum.domain.usecase

import android.app.Application
import com.devflow.factum.R
import com.devflow.factum.domain.model.Time
import com.devflow.factum.domain.repository.SharedPrefRepository
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val repository: SharedPrefRepository,
    private val appContext: Application
) {
    suspend fun execute(): List<Time> {
        val name = appContext.getString(R.string.shared_pref_main_notifications)

        return repository.read(name, emptySet<String>()).map {
            val data = it.split(":")

            Time(
                hour = data[0].toInt(),
                minute = data[1].toInt(),
                isActive = data[2].toBoolean()
            )
        }
    }
}