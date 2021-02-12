package com.github.bchang.valrec

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.bchang.valrec.databinding.ValuesTableRowItemBinding
import com.github.bchang.valrec.datastore.RowValue

internal class ValuesTableAdapter(private val dataSet: List<RowValue>) :
    RecyclerView.Adapter<ValuesTableAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ValuesTableRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rowValue: RowValue) {
            binding.timestamp.text = rowValue.timestamp.toString()
            binding.value.text = rowValue.value.toString()
        }
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
