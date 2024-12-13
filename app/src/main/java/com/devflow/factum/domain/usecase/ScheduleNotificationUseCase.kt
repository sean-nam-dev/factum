package com.devflow.factum.domain.usecase

import android.app.Application
import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.devflow.factum.NotificationWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val appContext: Application
) {
    fun execute(
        uniqueWorkName: String,
        hour: Int,
        minute: Int
    ) {
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(calculateDelay(hour, minute), TimeUnit.MILLISECONDS)
                .build()
        )
    }

    private fun calculateDelay(hour: Int, minute: Int): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val notificationTime = if (calendar.timeInMillis > currentTime) {
            calendar.timeInMillis
        } else {
            calendar.timeInMillis + TimeUnit.DAYS.toMillis(1)
        }

        return notificationTime - currentTime
    }
}