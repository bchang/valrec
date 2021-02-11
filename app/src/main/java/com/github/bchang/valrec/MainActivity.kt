package com.github.bchang.valrec

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.data.Set
import com.github.bchang.valrec.datastore.RowValue
import com.github.bchang.valrec.datastore.sample.SampleDataStore
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val viewModel by viewModels<DataStoreViewModel>()
        viewModel.getAllValues().observe(this, Observer {
            values_table.adapter = ValuesTableAdapter(it)
            values_chart.setChart(loadChart(it))
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

    fun loadChart(dataSet: List<RowValue>): Cartesian {
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.title("Values over time")
        cartesian.yAxis(0, "Value")
        cartesian.xAxis(0, "Date")

        val acSet = Set.instantiate()
        acSet.data(dataSet.map { row ->
            ValueDataEntry(row.timestamp.toString(), row.value)
        })

        val mapping = acSet.mapAs("{ x: 'x', value: 'value' }")
        val series = cartesian.line(mapping)
        series.name("Value")

        return cartesian
    }

    internal class DataStoreViewModel : ViewModel() {
        private val allValues = MutableLiveData<List<RowValue>>()

        fun getAllValues(): LiveData<List<RowValue>> {
            return allValues
        }

        fun load() {
            viewModelScope.launch {
                val dataStore = SampleDataStore()
                allValues.value = dataStore.getAll()
            }
        }
    }
}
