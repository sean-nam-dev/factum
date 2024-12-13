package com.devflow.factum.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devflow.factum.data.source.local.dao.ReadFactDao
import com.devflow.factum.domain.model.ReadFact

@Database(
    entities = [ReadFact::class],
    version = 1
)
abstract class ReadFactDatabase: RoomDatabase() {
    abstract val dao: ReadFactDao
}