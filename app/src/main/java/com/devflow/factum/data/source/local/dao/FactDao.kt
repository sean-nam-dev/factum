package com.devflow.factum.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.devflow.factum.domain.model.Fact
import kotlinx.coroutines.flow.Flow

@Dao
interface FactDao {

    @Upsert
    suspend fun insert(fact: Fact)

    @Delete
    suspend fun delete(fact: Fact)

    @Query("SELECT * FROM fact ORDER BY category ASC")
    fun getAll(): Flow<List<Fact>>

    @Query("SELECT EXISTS(SELECT 1 FROM Fact WHERE category = :category AND documentId = :documentId)")
    suspend fun isFactExists(category: String, documentId: String): Boolean
}