package com.github.bchang.valrec

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.data.Set
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.github.bchang.valrec.datastore.RowValue
import com.github.bchang.valrec.datastore.sample.SampleDataStore
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dataStore: SampleDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use dependency injection.
        dataStore = SampleDataStore()

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        values_table.adapter = ValuesTableAdapter(dataStore.getAll())
        values_chart.setChart(loadChart(dataStore.getAll()))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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
}
