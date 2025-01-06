package com.devflow.factum.domain.usecase

import android.app.Application
import androidx.work.WorkManager
import com.devflow.factum.domain.model.Time
import javax.inject.Inject

class CancelNotificationManagerUseCase @Inject constructor(
    private val appContext: Application
) {
    fun execute(time: Time) {
        WorkManager.getInstance(appContext).cancelUniqueWork(time.toString())
    }
}