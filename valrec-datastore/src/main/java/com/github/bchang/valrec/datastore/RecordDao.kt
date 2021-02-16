package com.github.bchang.valrec.datastore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
internal interface RecordDao {
    @Query("SELECT * FROM Record WHERE collectionId = :collectionId")
    fun getAll(collectionId: Long): List<Record>

    @Query("SELECT * FROM Record WHERE uid = :recordId")
    fun get(recordId: Long): Record

    @Insert
    fun insert(record: Record): Long

    @Transaction
    fun insertAndGet(record: Record): Record {
        val recordId = insert(record)
        return get(recordId)
    }
}
