package com.github.bchang.valrec.widget.table

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.bchang.valrec.widget.table.databinding.ValuesTableRowItemBinding
import com.github.bchang.valrec.datastore.Record

class ValuesTableAdapter :
    RecyclerView.Adapter<ValuesTableAdapter.ViewHolder>(),
    Observer<List<Record>> {
    var dataSet = emptyList<Record>()

    class ViewHolder(private val binding: ValuesTableRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record) {
            binding.timestamp.text = record.timestamp().toString()
            binding.value.text = record.value.toString()
        }
    }

    override fun onChanged(records: List<Record>) {
        this.dataSet = records
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ValuesTableRowItemBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}
