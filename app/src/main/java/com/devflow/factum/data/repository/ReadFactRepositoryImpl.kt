package com.devflow.factum.data.repository

import android.database.sqlite.SQLiteException
import com.devflow.factum.data.source.local.dao.ReadFactDao
import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.model.ReadFact
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.repository.ReadFactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadFactRepositoryImpl @Inject constructor(
    private val dao: ReadFactDao
): ReadFactRepository {

    override suspend fun insert(readFact: ReadFact): Result<Unit, DataError.Local> {
        return withContext(Dispatchers.IO) {
            try {
                dao.insert(readFact)
                Result.Success(Unit)
            } catch (e: OutOfMemoryError) {
                Result.Error(DataError.Local.DISK_FULL)
            } catch (e: SQLiteException) {
                Result.Error(DataError.Local.SQL)
            } catch (e: Exception) {
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun deleteAllByCategory(category: String) {
        return withContext(Dispatchers.IO) {
            dao.deleteAllByCategory(category)
        }
    }

    override suspend fun getAllByCategories(categories: Set<String>): List<ReadFact> {
        return withContext(Dispatchers.IO) {
            dao.getAllByCategories(categories)
        }
    }
}