package com.github.bchang.valrec.datastore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "ts_epoch_second") val tsEpochSecond: Long,
    @ColumnInfo(name = "int_value") val value: Int) {

    @Ignore val timestamp: Instant = Instant.ofEpochSecond(tsEpochSecond)
}
