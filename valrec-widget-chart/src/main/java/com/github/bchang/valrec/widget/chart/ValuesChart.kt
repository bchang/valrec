package com.github.bchang.valrec.widget.chart

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Interval
import com.anychart.scales.DateTime
import com.github.bchang.valrec.datastore.Record
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class ValuesChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    Observer<List<Record>> {

    private val anyChartView = AnyChartView(context, attrs, defStyleAttr)
    private val chart = AnyChart.line()
    private val dateTimeScale = DateTime.instantiate()

    init {
        chart.xScale(dateTimeScale)
        anyChartView.setChart(chart)
        addView(anyChartView)
    }

    override fun onChanged(records: List<Record>) {
        val acSet = Set.instantiate()
        chart.yScale().ticks().interval(1)
        // Strangely this will render labels all as "08:00:00",
        // definitely something to do with being in PST. It's labeling all
        // ticks as if converted back to GMT, then dropped any time mod so
        // that all labels are actually modeled by the same instant.
//        chart.xAxis(0).labels().format(
//            "{%value}{type:time}"
//        )
        // Works well for 24h - 8 ticks
        dateTimeScale.ticks().interval(Interval.HOUR, 3)
        acSet.data(records.map {
            ValueDataEntry(
                DATE_TIME_FORMATTER
                    .withZone(ZoneId.systemDefault())
                    .format(it.timestamp),
                it.value
            )
        })

        chart.removeAllSeries().addSeries(acSet)
    }

    companion object {
        private val DATE_TIME_FORMATTER =
            DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                // Fool the JS engine into thinking we're dealing in GMT times.
                // This seems to be the only solution unless I can find a way to
                // give the JS engine a different time zone for date format output.
                .appendLiteral('Z')
                .toFormatter()
    }
}
