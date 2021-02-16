package com.github.bchang.valrec.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import java.util.concurrent.Callable

@Database(
    entities = [
        Collection::class,
        Record::class
    ], version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
    abstract fun recordDao(): RecordDao

    fun init(): List<Record> {
        return runInTransaction(Callable {
            val collectionId = collectionDao().ensureAtLeastOneCollection().first().uid
            recordDao().getAll(collectionId)
        })
    }
}
