package com.devflow.factum.di

import com.devflow.factum.data.repository.FactRepositoryImpl
import com.devflow.factum.data.repository.FirestoreRepositoryImpl
import com.devflow.factum.data.repository.ReadFactRepositoryImpl
import com.devflow.factum.data.repository.SharedPrefRepositoryImpl
import com.devflow.factum.domain.repository.FactRepository
import com.devflow.factum.domain.repository.FirestoreRepository
import com.devflow.factum.domain.repository.ReadFactRepository
import com.devflow.factum.domain.repository.SharedPrefRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

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