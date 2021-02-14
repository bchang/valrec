package com.github.bchang.valrec.datastore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val appDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "valrec"
    ).build()
    val records = MutableLiveData<List<Record>>()

    fun load() {
        viewModelScope.launch {
            val records = withContext(Dispatchers.IO) {
                appDatabase.recordDao().getAll()
            }
            this@DataStoreViewModel.records.value = records
        }
    }

    fun insert(record: Record) {
        viewModelScope.launch {
            val records = withContext(Dispatchers.IO) {
                appDatabase.recordDao().insertAndGetAll(record)
            }
            this@DataStoreViewModel.records.value = records
        }
    }
}
