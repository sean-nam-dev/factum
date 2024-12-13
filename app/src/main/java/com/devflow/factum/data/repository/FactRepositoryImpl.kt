package com.devflow.factum.data.repository

import android.database.sqlite.SQLiteException
import com.devflow.factum.data.source.local.dao.FactDao
import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.repository.FactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FactRepositoryImpl @Inject constructor(
    private val dao: FactDao
): FactRepository {

    override suspend fun insert(fact: Fact): Result<Unit, DataError.Local> {
        return withContext(Dispatchers.IO) {
            try {
                dao.insert(fact)
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

    override suspend fun delete(fact: Fact): Result<Unit, DataError.Local> {
        return withContext(Dispatchers.IO) {
            try {
                dao.delete(fact)
                Result.Success(Unit)
            } catch (e: SQLiteException) {
                Result.Error(DataError.Local.SQL)
            } catch (e: Exception) {
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override fun getAll(): Flow<List<Fact>> = dao.getAll()

    override suspend fun isFactExists(
        category: String,
        documentId: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            dao.isFactExists(category, documentId)
        }
    }
}