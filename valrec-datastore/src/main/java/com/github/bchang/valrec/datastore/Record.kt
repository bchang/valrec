package com.github.bchang.valrec.datastore

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.Instant

fun createRecord(
    collectionId: Long, timestamp: Instant, value: Int
): Record {
    return Record(
        0, collectionId, timestamp.toEpochMilli(), value
    )
}

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = Collection::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("collectionId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Record internal constructor(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val collectionId: Long,
    val tsEpochMilli: Long,
    val value: Int
) {
    @Ignore
    val timestamp: Instant = Instant.ofEpochMilli(tsEpochMilli)
}
