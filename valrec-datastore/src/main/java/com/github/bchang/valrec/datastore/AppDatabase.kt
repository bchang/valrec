package com.github.bchang.valrec.datastore

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}
