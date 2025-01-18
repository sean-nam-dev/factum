package com.devflow.factum.domain.usecase

import android.app.Application
import com.devflow.factum.R
import com.devflow.factum.domain.model.Time
import com.devflow.factum.domain.repository.SharedPrefRepository
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val repository: SharedPrefRepository,
    private val appContext: Application
) {
    suspend fun execute(time: Time) {
        val name = appContext.getString(R.string.shared_pref_main_notifications)
        val data = repository
            .read(name, emptySet<String>())
            .minus(time.toString())

        repository.write(
            name = name,
            data = data
        )
    }
}