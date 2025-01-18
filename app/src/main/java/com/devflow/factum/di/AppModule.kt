package com.devflow.factum.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.speech.tts.TextToSpeech
import androidx.room.Room
import com.devflow.factum.R
import com.devflow.factum.data.source.local.dao.FactDao
import com.devflow.factum.data.source.local.dao.ReadFactDao
import com.devflow.factum.data.source.local.database.FactDatabase
import com.devflow.factum.data.source.local.database.ReadFactDatabase
import com.devflow.factum.navigation.DefaultNavigator
import com.devflow.factum.navigation.Destination
import com.devflow.factum.navigation.Navigator
import com.devflow.factum.util.ContextResourceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContextResourceManager(
        appContext: Application
    ): ContextResourceManager = ContextResourceManager(appContext)

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        firestore.firestoreSettings = settings

        return firestore
    }

    @Provides
    @Singleton
    fun provideFactDao(appContext: Application): FactDao {
        val db = Room.databaseBuilder(
            appContext,
            FactDatabase::class.java,
            "fact.db"
        ).build()

        return db.dao
    }

    @Provides
    @Singleton
    fun provideReadFactDao(appContext: Application): ReadFactDao {
        val db = Room.databaseBuilder(
            appContext,
            ReadFactDatabase::class.java,
            "read_fact.db"
        ).build()

        return db.dao
    }

    @Provides
    @Singleton
    fun provideSharedPrefMain(appContext: Application): SharedPreferences {
        val name = appContext.getString(R.string.shared_pref_main)
        return appContext.getSharedPreferences(name, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideTextToSpeech(appContext: Application): TextToSpeech =
        TextToSpeech(appContext, null)

    @Provides
    @Singleton
    fun provideNavigator(
        contextResourceManager: ContextResourceManager,
        mainSharedPreferences: SharedPreferences
    ): Navigator {
        val name = contextResourceManager.getString(R.string.shared_pref_main_launch)
        val isFirstLaunch = mainSharedPreferences.getBoolean(name, true)

        return DefaultNavigator(
            startDestination = if (isFirstLaunch) {
                Destination.StartGraph
            } else {
                Destination.HomeGraph
            }
        )
    }

    @Provides
    @Singleton
    fun provideDailyNotificationChannel(
        contextResourceManager: ContextResourceManager
    ): NotificationChannel {
        val id = "1000"
        val name = contextResourceManager.getString(R.string.daily_facts)
        val description = contextResourceManager.getString(R.string.notifications_of_daily_facts)

        val channel = NotificationChannel(
            id,
            name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setDescription(description)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 200, 100, 300)
        }

        return channel
    }

    @Provides
    @Singleton
    fun provideNotificationManager(
        appContext: Application,
        dailyNotificationChannel: NotificationChannel
    ): NotificationManager = appContext.getSystemService(NotificationManager::class.java).apply {
        createNotificationChannel(dailyNotificationChannel)
    }
}