package com.github.bchang.valrec

import android.app.Application
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.data.Set
import com.github.bchang.valrec.databinding.ActivityMainBinding
import com.github.bchang.valrec.datastore.DataStore
import com.github.bchang.valrec.datastore.Record
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
        val valuesChart: AnyChartView = binding.root.findViewById(R.id.values_chart)
        viewModel.getAllRecords().observe(this, Observer {
            valuesTable.adapter = ValuesTableAdapter(it)
            valuesChart.setChart(loadChart(it))
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

    fun loadChart(records: List<Record>): Cartesian {
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.title("Values over time")
        cartesian.yAxis(0, "Value")
        cartesian.xAxis(0, "Date")

        val acSet = Set.instantiate()
        acSet.data(records.map {
            ValueDataEntry(it.timestamp().toString(), it.value)
        })

        val mapping = acSet.mapAs("{ x: 'x', value: 'value' }")
        val series = cartesian.line(mapping)
        series.name("Value")

        return cartesian
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
