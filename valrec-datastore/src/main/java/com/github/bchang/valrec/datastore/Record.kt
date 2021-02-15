package com.github.bchang.valrec.datastore

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.Instant

fun createRecord(timestamp: Instant, value: Int): Record {
    return Record(0, timestamp.toEpochMilli(), value)
}

@Entity
data class Record internal constructor(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val tsEpochMilli: Long,
    val value: Int
) {

    @Ignore
    val timestamp: Instant = Instant.ofEpochMilli(tsEpochMilli)
}
