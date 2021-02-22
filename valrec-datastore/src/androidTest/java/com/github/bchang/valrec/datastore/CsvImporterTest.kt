package com.github.bchang.valrec.datastore

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.jimfs.Jimfs
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith
import java.io.IOException
import java.nio.file.Files.newOutputStream
import java.nio.file.Path
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

@RunWith(AndroidJUnit4::class)
class CsvImporterTest {
    private lateinit var db: AppDatabase

    private lateinit var testDir: Path

    @get:Rule
    val name = TestName()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Before
    fun createTestDir() {
        testDir = Jimfs.newFileSystem().getPath("/")
        newOutputStream(testDir.resolve("test.csv"))
            .bufferedWriter(Charsets.UTF_8).use {
                it.appendLine("Timestamp,Value")
                    .appendLine("2017-07-21T08:00:00.000Z,2")
            }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun importCsv() {
        CsvImporter(db).importCsvFiles(testDir, ISO_DATE_TIME)

        val collection = db.collectionDao().get(1)
        assertThat(collection.name).isEqualTo("test.csv")
        assertThat(db.recordDao().getAll(collection.uid))
            .containsExactly(
                Record(
                    1,
                    collection.uid,
                    toEpochMilli("2017-07-21T08:00:00Z"),
                    2
                )
            )
    }

    @Test
    fun importCsv_localDateTimeFormat() {
        newOutputStream(testDir.resolve("test.csv"))
            .bufferedWriter(Charsets.UTF_8).use {
                it.appendLine("Timestamp,Value")
                    .appendLine("2017-07-21T00:00:00.000,2")
                    .appendLine("2018-01-21T00:00:00.000,4")
            }

        CsvImporter(db).importCsvFiles(
            testDir,
            ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("America/Los_Angeles"))
        )

        val collection = db.collectionDao().get(1)
        assertThat(collection.name).isEqualTo("test.csv")
        assertThat(db.recordDao().getAll(collection.uid))
            .containsExactly(
                Record(
                    1,
                    collection.uid,
                    // Daylight time - offset is -0700
                    toEpochMilli("2017-07-21T07:00:00Z"),
                    2
                ),
                Record(
                    2,
                    collection.uid,
                    // Standard time - offset is -0800
                    toEpochMilli("2018-01-21T08:00:00Z"),
                    4
                )
            )
    }

    @Test
    fun importCsv_doesNotOverwriteExistingCollection() {
        db.collectionDao().insert(createCollection("test.csv"))

        CsvImporter(db).importCsvFiles(testDir, ISO_DATE_TIME)

        val collection = db.collectionDao().get(1)
        assertThat(collection.name).isEqualTo("test.csv")
        assertThat(db.recordDao().getAll(collection.uid)).isEmpty()
    }

    @Test
    fun importCsv_multipleRecords() {
        newOutputStream(testDir.resolve("test.csv"))
            .bufferedWriter(Charsets.UTF_8).use {
                it.appendLine("Timestamp,Value")
                    .appendLine("2017-07-21T08:00:00.000Z,2")
                    .appendLine("2017-08-03T17:12:55.360Z,4")
                    .appendLine("2018-09-01T21:59:19.420Z,")
                    .appendLine("2019-03-27T06:48:00.846Z,8")
            }

        CsvImporter(db).importCsvFiles(testDir, ISO_DATE_TIME)

        val collection = db.collectionDao().get(1)
        assertThat(collection.name).isEqualTo("test.csv")
        assertThat(db.recordDao().getAll(collection.uid))
            .containsExactly(
                Record(
                    1,
                    collection.uid,
                    toEpochMilli("2017-07-21T08:00:00Z"),
                    2
                ),
                Record(
                    2,
                    collection.uid,
                    toEpochMilli("2017-08-03T17:12:55.360Z"),
                    4
                ),
                Record(
                    3,
                    collection.uid,
                    toEpochMilli("2019-03-27T06:48:00.846Z"),
                    8
                )
            )
    }

    private fun toEpochMilli(isoDateTime: String): Long {
        return Instant.parse(isoDateTime).toEpochMilli()
    }
}
