package com.devflow.factum.domain.usecase

import android.app.Application
import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.devflow.factum.data.source.remote.NotificationWorker
import com.devflow.factum.domain.model.Time
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleNotificationManagerUseCase @Inject constructor(
    private val appContext: Application
) {
    fun execute(time: Time) {
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            time.toString(),
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(calculateDelay(time.hour, time.minute), TimeUnit.MILLISECONDS)
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