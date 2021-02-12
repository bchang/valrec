package com.github.bchang.valrec.datastore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Record(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "ts_epoch_second") val tsEpochSecond: Long,
    @ColumnInfo(name = "int_value") val value: Int) {

    fun timestamp(): Instant {
        return Instant.ofEpochSecond(tsEpochSecond)
    }
}
