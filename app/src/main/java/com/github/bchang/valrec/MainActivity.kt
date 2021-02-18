package com.github.bchang.valrec

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.bchang.valrec.databinding.ActivityMainBinding
import com.github.bchang.valrec.datastore.DataStoreViewModel
import com.github.bchang.valrec.datastore.createRecord
import com.github.bchang.valrec.widget.chart.ValuesChart
import com.github.bchang.valrec.widget.table.ValuesTableAdapter
import java.time.Instant
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        // TODO: total hack - hardcoded id for a default collection, since multiple collections are
        //  not yet supported.
        private const val COLLECTION_ID = 1L
    }
    private lateinit var binding: ActivityMainBinding
    val rand = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val viewModel by viewModels<DataStoreViewModel>()

        binding.fab.setOnClickListener {
            viewModel.insert(createRecord(COLLECTION_ID, Instant.now(), rand.nextInt(16)))
        }

        val valuesTable: RecyclerView = binding.root.findViewById(R.id.values_table)
        val valuesTableAdapter = ValuesTableAdapter()
        valuesTable.adapter = valuesTableAdapter

        val valuesChart: ValuesChart = binding.root.findViewById(R.id.values_chart)

        viewModel.records.observe(this, valuesTableAdapter)
        viewModel.records.observe(this, valuesChart)
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
}
