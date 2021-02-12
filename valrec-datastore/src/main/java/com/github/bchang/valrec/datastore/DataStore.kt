package com.github.bchang.valrec.datastore

import android.content.Context
import androidx.room.Room

class DataStore(applicationContext: Context) {
    private val appDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "valrec"
    ).build()

    suspend fun getAll(): List<Record> {
        return appDatabase.recordDao().getAll()
    }
}
