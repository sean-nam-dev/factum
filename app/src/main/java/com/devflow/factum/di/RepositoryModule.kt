package com.devflow.factum.di

import android.annotation.SuppressLint
import androidx.hilt.work.WorkerAssistedFactory
import androidx.work.ListenableWorker
import com.devflow.factum.data.repository.FactRepositoryImpl
import com.devflow.factum.data.repository.FirestoreRepositoryImpl
import com.devflow.factum.data.repository.ReadFactRepositoryImpl
import com.devflow.factum.data.repository.SharedPrefRepositoryImpl
import com.devflow.factum.data.source.remote.NotificationWorker
import com.devflow.factum.data.source.remote.NotificationWorker_AssistedFactory
import com.devflow.factum.domain.repository.FactRepository
import com.devflow.factum.domain.repository.FirestoreRepository
import com.devflow.factum.domain.repository.ReadFactRepository
import com.devflow.factum.domain.repository.SharedPrefRepository
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @SuppressLint("RestrictedApi")
    @Binds
    @IntoMap
    @WorkerKey(NotificationWorker::class)
    abstract fun bindNotificationWorker(factory: NotificationWorker_AssistedFactory): WorkerAssistedFactory<out ListenableWorker>

    @Binds
    @Singleton
    abstract fun bindFirestoreRepository(
        firestoreRepositoryImpl: FirestoreRepositoryImpl
    ): FirestoreRepository

    @Binds
    @Singleton
    abstract fun bindFactRepository(
        factRepositoryImpl: FactRepositoryImpl
    ): FactRepository

    @Binds
    @Singleton
    abstract fun bindReadFactRepository(
        readFactRepositoryImpl: ReadFactRepositoryImpl
    ): ReadFactRepository

    @Binds
    @Singleton
    abstract fun bindCategorySharedPrefRepository(
        categorySharedPrefRepositoryImpl: SharedPrefRepositoryImpl
    ): SharedPrefRepository
}