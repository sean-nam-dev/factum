package com.devflow.factum.data.repository

import android.content.SharedPreferences
import com.devflow.factum.domain.repository.SharedPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPrefRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferences
): SharedPrefRepository {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> write(name: String, data: T) {
        withContext(Dispatchers.IO) {
            with(sharedPref.edit()) {
                when(data) {
                    is Set<*> -> putStringSet(name, data as Set<String>)
                    is Int -> putInt(name, data)
                    is String -> putString(name, data)
                    is Boolean -> putBoolean(name, data)
                    else -> throw IllegalArgumentException()
                }.apply()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> read(name: String, defaultValue: T): T {
        return withContext(Dispatchers.IO) {
            when(defaultValue) {
                is Set<*> -> sharedPref.getStringSet(name, defaultValue as Set<String>) as T
                is Int -> sharedPref.getInt(name, defaultValue) as T
                is String -> sharedPref.getString(name, defaultValue) as T
                is Boolean -> sharedPref.getBoolean(name, defaultValue) as T
                else -> throw IllegalArgumentException()
            }
        }
    }
}