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
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun collectionDao(): CollectionDao
    internal abstract fun recordDao(): RecordDao

    fun init(): List<Record> {
        return runInTransaction(Callable {
            val collectionId = collectionDao().ensureAtLeastOneCollection().first().uid
            recordDao().getAll(collectionId)
        })
    }

    fun insertCollection(name: String, records: Sequence<Record>) {
        return runInTransaction(Callable {
            if (collectionDao().get(name) != null) {
                throw IllegalArgumentException("Collection \"$name\" already exists")
            }

            val collectionId = collectionDao().insert(createCollection(name))
            records.forEach {
                recordDao().insert(it.withCollectionId(collectionId))
            }
        })
    }
}
