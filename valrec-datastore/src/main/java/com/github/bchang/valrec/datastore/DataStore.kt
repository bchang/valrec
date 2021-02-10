package com.github.bchang.valrec.datastore

import java.time.Instant

data class RowValue(val timestamp: Instant, val value: Number)

interface DataStore {
    fun getAll(): List<RowValue>
}
