package com.github.bchang.valrec.datastore

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataStore(applicationContext: Context) {
    private val appDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "valrec"
    ).build()

    suspend fun getAll(): List<Record> {
        return withContext(Dispatchers.IO) {
            appDatabase.recordDao().getAll()
        }
    }

    suspend fun insert(record: Record) {
        return withContext(Dispatchers.IO) {
            appDatabase.recordDao().insert(record)
        }
    }
}
