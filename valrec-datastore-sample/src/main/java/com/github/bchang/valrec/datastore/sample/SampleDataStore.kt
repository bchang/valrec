package com.github.bchang.valrec.datastore.sample

import com.github.bchang.valrec.datastore.DataStore
import com.github.bchang.valrec.datastore.RowValue
import java.time.Instant

class SampleDataStore : DataStore {
    override fun getAll(): List<RowValue> {
        val timestamp = Instant.ofEpochSecond(1612998987)
        timestamp.plusSeconds(300)
        return listOf(
            RowValue(timestamp, 1),
            RowValue(timestamp.plusSeconds(300), 2),
            RowValue(timestamp.plusSeconds(600), 4),
            RowValue(timestamp.plusSeconds(900), 8))
    }
}
