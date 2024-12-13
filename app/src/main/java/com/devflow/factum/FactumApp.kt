package com.devflow.factum

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devflow.factum.util.Temp
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@HiltAndroidApp
class FactumApp: Application() {
    override fun onCreate() {
        super.onCreate()

//        getSystemService<NotificationManager>()!!.run {
//            createNotificationChannel(
//                NotificationChannel(
//                    R.id.daily_notification_channel.toString(),
//                    this@FactumApp.getString(R.string.daily_facts),
//                    NotificationManager.IMPORTANCE_HIGH
//                )
//            )
//        }

        NotificationScheduleManager(this)
    }
}

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_id"

    val intent = Intent(context, MainActivity::class.java).apply {
        data = "${context.getString(R.string.deeplink_domain)}/130".toUri()
    }
    val pendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
    }

    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Simple notification")
            .setContentText("Some message")
            .setSmallIcon(R.drawable.ic_round_check_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle("Title")
                    .bigText(Temp.getFact().text)
            )
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        val channelId = "1000"
        val title = "А вы знали?"
        val text = "Нажмите, чтобы узнать больше!"

        val channel = NotificationChannel(
            channelId,
            applicationContext.getString(R.string.daily_facts),
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            data = "${applicationContext.getString(R.string.deeplink_domain)}/130".toUri()
        }
        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_round_check_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle("Заголовок")
                    .bigText(Temp.getFact().text)
            )
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}

class NotificationScheduleManager(appContext: Context) {
    val morningRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(30000, TimeUnit.MILLISECONDS)
        .build()

    val eveningRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(90000, TimeUnit.MILLISECONDS)
        .build()

    init {
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "morning_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            morningRequest
        )
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "evening_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            eveningRequest
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