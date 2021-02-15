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

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
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
        val recordId = recordDao.insert(
            Record(0, 2, 3))
        assertThat(recordId).isEqualTo(1)
        assertThat(recordDao.getAll()).containsExactly(
            Record(1, 2, 3))
    }

    @Test
    fun insertAndGet() {
        val record1 = recordDao.insertAndGet(
            Record(0, 2, 3))
        assertThat(record1).isEqualTo(
            Record(1, 2, 3))

        val record2 = recordDao.insertAndGet(
            Record(0, 2, 3))
        assertThat(record2).isEqualTo(
            Record(2, 2, 3))

        assertThat(recordDao.getAll()).containsExactly(
            Record(1, 2, 3),
            Record(2, 2, 3))
    }
}
