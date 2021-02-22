package com.github.bchang.valrec.datastore

import android.util.Log
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.time.format.DateTimeFormatter

class CsvImporter(
    private val appDatabase: AppDatabase
) {

    companion object {
        private const val TAG = "CsvImporter"
    }

    fun importCsvFiles(dir: Path, fmt: DateTimeFormatter) {
        check(Files.exists(dir)) { "$dir doesn't exist" }
        check(Files.isDirectory(dir)) { "$dir is not a directory" }

        val csvFiles = Files.list(dir)
            .filter {
                Files.isRegularFile(it) && it.fileName.toString().endsWith(".csv")
            }
        for (csvFile in csvFiles) {
            try {
                importCsv(csvFile, fmt)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to import $csvFile", e)
            }
        }
    }

    private fun importCsv(csvFile: Path, fmt: DateTimeFormatter) {
        appDatabase.insertCollection(
            csvFile.fileName.toString(),
            parseCsv(csvFile)
                .filter { it[1].isNotEmpty() }
                .map {
                    check(it.size == 2)
                    val timestamp = Instant.from(fmt.parse(it[0]))
                    val value = Integer.parseInt(it[1])
                    createRecord(-1, timestamp, value)
                }
        )
    }

    private fun parseCsv(csvFile: Path): Sequence<List<String>> {
        return Files.newInputStream(csvFile).bufferedReader(Charsets.UTF_8)
            .lineSequence()
            .drop(1)
            .map { it.split(",") }
    }
}
