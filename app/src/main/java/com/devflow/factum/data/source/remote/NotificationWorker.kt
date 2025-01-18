package com.devflow.factum.data.source.remote

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext : Context,
    @Assisted params : WorkerParameters,
    private val dailyNotificationHelper: DailyNotificationHelper
): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        dailyNotificationHelper.execute()

        return Result.success()
    }
}