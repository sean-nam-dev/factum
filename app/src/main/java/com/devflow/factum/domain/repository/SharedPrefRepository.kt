package com.devflow.factum.domain.repository

interface SharedPrefRepository {

    suspend fun <T> write(name: String, data: T)

    suspend fun <T> read(name: String, defaultValue: T): T
}