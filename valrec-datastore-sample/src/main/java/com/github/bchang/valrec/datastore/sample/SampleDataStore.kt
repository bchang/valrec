package com.github.bchang.valrec.datastore.sample

import com.github.bchang.valrec.datastore.DataStore
import com.github.bchang.valrec.datastore.RowValue
import java.time.Instant
import kotlin.math.pow

class SampleDataStore : DataStore {
    override fun getAll(): List<RowValue> {
        val timestamp = Instant.ofEpochSecond(1612998987)
        return (0L..19)
            .map { i ->
                RowValue(
                    timestamp.plusSeconds(i * 300),
                    2.0.pow(i.toDouble()).toInt()
                )
            }
    }
}
