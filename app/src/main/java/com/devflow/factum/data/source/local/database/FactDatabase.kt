package com.devflow.factum.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devflow.factum.data.source.local.dao.FactDao
import com.devflow.factum.domain.model.Fact

@Database(
    entities = [Fact::class],
    version = 1
)
abstract class FactDatabase: RoomDatabase() {
    abstract val dao: FactDao
}