package com.github.bchang.valrec

import android.app.Application
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.github.bchang.valrec.databinding.ActivityMainBinding
import com.github.bchang.valrec.datastore.DataStore
import com.github.bchang.valrec.datastore.Record
import com.github.bchang.valrec.widget.chart.ValuesChart
import com.github.bchang.valrec.widget.table.ValuesTableAdapter
import kotlinx.coroutines.launch
import java.time.Instant

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val viewModel by viewModels<DataStoreViewModel>()

        binding.fab.setOnClickListener {
            viewModel.addRecord()
        }

        val valuesTable: RecyclerView = binding.root.findViewById(R.id.values_table)
        val valuesTableAdapter = ValuesTableAdapter()
        valuesTable.adapter = valuesTableAdapter

        val valuesChart: ValuesChart = binding.root.findViewById(R.id.values_chart)

        viewModel.getAllRecords().observe(this, Observer {
            valuesTableAdapter.setRecords(it)
            valuesChart.setRecords(it)
        })
        viewModel.load()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    internal class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
        private val dataStore = DataStore(application)
        private val allRecords = MutableLiveData<List<Record>>()

        fun getAllRecords(): LiveData<List<Record>> {
            return allRecords
        }

        fun addRecord() {
            viewModelScope.launch {
                dataStore.insert(Record(0, Instant.now().epochSecond, 3))
                allRecords.value = dataStore.getAll()
            }
        }

        fun load() {
            viewModelScope.launch {
                allRecords.value = dataStore.getAll()
            }
        }
    }
}
