package com.github.bchang.valrec.widget.chart

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.github.bchang.valrec.datastore.Record

class ValuesChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    Observer<List<Record>> {

    private val anyChartView = AnyChartView(context, attrs, defStyleAttr)
    private val cartesian = AnyChart.line()

    init {
        addView(anyChartView)

        cartesian.animation(true)
        cartesian.title("Values over time")
        cartesian.yAxis(0, "Value")
        cartesian.xAxis(0, "Date")
        anyChartView.setChart(cartesian)
    }

    override fun onChanged(records: List<Record>) {
        val acSet = Set.instantiate()
        acSet.data(records.map {
            ValueDataEntry(it.timestamp().toString(), it.value)
        })

        cartesian.removeAllSeries().addSeries(acSet)
    }
}
