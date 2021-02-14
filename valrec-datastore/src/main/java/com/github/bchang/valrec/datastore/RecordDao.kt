package com.github.bchang.valrec.datastore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface RecordDao {
    @Query("SELECT * FROM record")
    fun getAll(): List<Record>

    @Insert
    fun insert(record: Record)
}
