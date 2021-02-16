package com.github.bchang.valrec.datastore

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

fun createCollection(name: String): Collection {
    return Collection(0, name)
}

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Collection internal constructor (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val name: String
)
