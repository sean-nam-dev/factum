package com.devflow.factum.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devflow.factum.domain.model.ReadFact

@Dao
interface ReadFactDao {

    @Upsert
    suspend fun insert(readFact: ReadFact)

    @Query("DELETE FROM ReadFact WHERE category = :category")
    suspend fun deleteAllByCategory(category: String)

    @Query("SELECT * FROM ReadFact WHERE category in (:categories)")
    suspend fun getAllByCategories(categories: Set<String>): List<ReadFact>
}