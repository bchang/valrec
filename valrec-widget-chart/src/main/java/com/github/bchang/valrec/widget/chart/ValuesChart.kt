package com.github.bchang.valrec.widget.chart

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.data.Set
import com.github.bchang.valrec.datastore.Record

class ValuesChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val anyChartView = AnyChartView(context, attrs, defStyleAttr)
    init {
        addView(anyChartView)
    }

    fun setRecords(records: List<Record>) {
        anyChartView.setChart(loadChart(records))
    }

    private fun loadChart(records: List<Record>): Cartesian {
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
}
