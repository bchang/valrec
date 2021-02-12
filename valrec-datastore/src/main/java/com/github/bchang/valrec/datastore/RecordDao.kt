package com.github.bchang.valrec.datastore

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface RecordDao {
    @Query("SELECT * FROM record")
    suspend fun getAll(): List<Record>
}
