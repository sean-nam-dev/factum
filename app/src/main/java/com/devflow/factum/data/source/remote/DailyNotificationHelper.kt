package com.devflow.factum.data.source.remote

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.devflow.factum.MainActivity
import com.devflow.factum.R
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.usecase.DeleteAllByCategoryReadFactUseCase
import com.devflow.factum.domain.usecase.GetCollectionSizeUseCase
import com.devflow.factum.domain.usecase.GetFactFromServerUseCase
import com.devflow.factum.domain.usecase.GetReadFactUseCase
import com.devflow.factum.domain.usecase.RandomizerUseCase
import com.devflow.factum.domain.usecase.ReadCategoryUseCase
import com.devflow.factum.domain.usecase.WriteReadFactUseCase
import javax.inject.Inject
import kotlin.random.Random

class DailyNotificationHelper @Inject constructor(
    private val appContext: Application,
    private val readCategoryUseCase: ReadCategoryUseCase,
    private val getReadFactUseCase: GetReadFactUseCase,
    private val writeReadFactUseCase: WriteReadFactUseCase,
    private val randomizerUseCase: RandomizerUseCase,
    private val getFactFromServerUseCase: GetFactFromServerUseCase,
    private val notificationManager: NotificationManager,
    private val getCollectionSizeUseCase: GetCollectionSizeUseCase,
    private val deleteAllByCategoryReadFactUseCase: DeleteAllByCategoryReadFactUseCase
) {
    suspend fun execute() {
        val fact = getFact()

        if (fact != null) {
            val intent = Intent(appContext, MainActivity::class.java).apply {
                data = "${appContext.getString(R.string.deeplink_domain)}/${fact.factBase.documentId}/${fact.factBase.category}".toUri()
            }
            val pendingIntent = TaskStackBuilder.create(appContext).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notification = NotificationCompat.Builder(appContext, "1000")
                .setContentTitle(appContext.getString(R.string.did_you_know))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .setBigContentTitle(fact.title)
                        .bigText(fact.text)
                )
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(Random.nextInt(), notification)
        }
    }

    private suspend fun getFact(): Fact? {
        val categorySet = readCategoryUseCase.execute()
        val readFactMap = getReadFactUseCase.execute(categorySet)

        val randomCategory = categorySet.random()
        val readFactValues = readFactMap[randomCategory].orEmpty()

        val randomId = randomizerUseCase.getRandomUniqueNumber(readFactValues)

        return when(val factOrError = getFactFromServerUseCase.execute(randomCategory, randomId)) {
            is Result.Success -> {
                when(writeReadFactUseCase.execute(factOrError.data.factBase)) {
                    is Result.Error -> {}
                    is Result.Success -> {
                        when(val getCollectionSizeResult = getCollectionSizeUseCase.execute(factOrError.data.factBase.category)) {
                            is Result.Error -> {}
                            is Result.Success -> {
                                val readFactSize = readFactValues.size.toLong()

                                if (readFactSize == getCollectionSizeResult.data) {
                                    deleteAllByCategoryReadFactUseCase.execute(factOrError.data.factBase.category)
                                }
                            }
                        }
                    }
                }

                factOrError.data
            }
            is Result.Error -> {
                null
            }
        }
    }
}