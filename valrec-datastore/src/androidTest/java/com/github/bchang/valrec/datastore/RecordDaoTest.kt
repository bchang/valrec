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
import kotlin.properties.Delegates.notNull

@RunWith(AndroidJUnit4::class)
class RecordDaoTest {
    private val timestamp = Instant.ofEpochMilli(1613430549123)
    private lateinit var db: AppDatabase
    private var collectionId by notNull<Long>()
    private lateinit var recordDao: RecordDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        collectionId = db.collectionDao().ensureAtLeastOneCollection().first().uid
        recordDao = db.recordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert() {
        val recordId = recordDao.insert(createRecord(collectionId, timestamp, 3))
        assertThat(recordId).isEqualTo(1)
        assertThat(recordDao.getAll(collectionId)).containsExactly(
            Record(1, collectionId, timestamp.toEpochMilli(), 3))
    }

    @Test
    fun insertAndGet() {
        assertThat(recordDao.insertAndGet(createRecord(collectionId, timestamp, 3)))
            .isEqualTo(Record(1, collectionId, timestamp.toEpochMilli(), 3))

        assertThat(recordDao.insertAndGet(createRecord(collectionId, timestamp, 3)))
            .isEqualTo(Record(2, collectionId, timestamp.toEpochMilli(), 3))

        assertThat(recordDao.getAll(collectionId)).containsExactly(
            Record(1, collectionId, timestamp.toEpochMilli(), 3),
            Record(2, collectionId, timestamp.toEpochMilli(), 3))
    }
}
