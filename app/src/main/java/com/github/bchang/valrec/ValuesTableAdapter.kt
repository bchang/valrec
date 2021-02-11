package com.github.bchang.valrec

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.bchang.valrec.datastore.RowValue

internal class ValuesTableAdapter(private val dataSet: List<RowValue>) :
    RecyclerView.Adapter<ValuesTableAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timestampTextView: TextView = view.findViewById(R.id.timestamp)
        val valueTextView: TextView = view.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.values_table_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.timestampTextView.text = dataSet[position].timestamp.toString()
        viewHolder.valueTextView.text = dataSet[position].value.toString()
    }

    override fun getItemCount() = dataSet.size
}
