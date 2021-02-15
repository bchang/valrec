package com.github.bchang.valrec.datastore

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    val timestamp = Instant.ofEpochMilli(1613430549123)
    private lateinit var db: AppDatabase
    private lateinit var recordDao: RecordDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        recordDao = db.recordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert() {
        val recordId = recordDao.insert(createRecord(timestamp, 3))
        assertThat(recordId).isEqualTo(1)
        assertThat(recordDao.getAll()).containsExactly(
            Record(1, timestamp.toEpochMilli(), 3))
    }

    @Test
    fun insertAndGet() {
        val record1 = recordDao.insertAndGet(createRecord(timestamp, 3))
        assertThat(record1).isEqualTo(
            Record(1, timestamp.toEpochMilli(), 3))

        val record2 = recordDao.insertAndGet(createRecord(timestamp, 3))
        assertThat(record2).isEqualTo(
            Record(2, timestamp.toEpochMilli(), 3))

        assertThat(recordDao.getAll()).containsExactly(
            Record(1, timestamp.toEpochMilli(), 3),
            Record(2, timestamp.toEpochMilli(), 3))
    }
}
