package com.github.bchang.valrec.datastore.sample

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class SampleDataStoreTest {

    @Test
    fun notEmpty() {
        val dataStore = SampleDataStore()
        assertThat(dataStore.getAll()).isNotEmpty()
    }

    @Test
    fun values() {
        val dataStore = SampleDataStore()
        val values = dataStore.getAll().map { rowValue -> rowValue.value }
        assertThat(values).isEqualTo(listOf(1, 2, 4, 8))
    }
}
