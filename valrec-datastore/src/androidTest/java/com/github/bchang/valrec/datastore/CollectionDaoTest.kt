package com.github.bchang.valrec.datastore

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CollectionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var collectionDao: CollectionDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        collectionDao = db.collectionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert() {
        assertThat(collectionDao.insert(createCollection("foo"))) .isEqualTo(1)
        assertThat(collectionDao.insert(createCollection("bar"))) .isEqualTo(2)

        assertThat(collectionDao.getNumCollections()) .isEqualTo(2)
        assertThat(collectionDao.getAll())
            .containsExactly(
                Collection(1, "foo"),
                Collection(2, "bar")
            )
        assertThat(collectionDao.get(2))
            .isEqualTo(Collection(2, "bar"))
    }

    @Test
    fun insert_uniqueNameEnforced() {
        collectionDao.insert(createCollection("foo"))

        val ex = assertThrows(SQLiteConstraintException::class.java) {
            collectionDao.insert(createCollection("foo"))
        }
        assertThat(ex).hasMessageThat().contains("Collection.name")
        assertThat(ex).hasMessageThat().contains("SQLITE_CONSTRAINT_UNIQUE")
    }

    @Test
    fun ensureAtLeastOneCollection() {
        assertThat(collectionDao.getNumCollections()).isEqualTo(0)

        assertThat(collectionDao.ensureAtLeastOneCollection())
            .containsExactly(Collection(1, ""))

        assertThat(collectionDao.ensureAtLeastOneCollection())
            .containsExactly(Collection(1, ""))
    }

    @Test
    fun ensureAtLeastOneCollection_2collectionsAlready() {
        collectionDao.insert(createCollection("foo"))
        collectionDao.insert(createCollection("bar"))

        assertThat(collectionDao.ensureAtLeastOneCollection())
            .containsExactly(
                Collection(1, "foo"),
                Collection(2, "bar")
            )
    }
}