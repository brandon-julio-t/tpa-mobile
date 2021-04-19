package edu.bluejack20_2.braven.pages.user_statistics

import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import java.util.*
import javax.inject.Inject

class UserStatisticsController @Inject constructor() {
    fun bind(fragment: UserStatisticsFragment) {
        val binding = fragment.binding

        binding.chart.setChart(
            AnyChart.pie3d().apply {
                data(
                    listOf(
                        ValueDataEntry("Haha", Random().nextInt()),
                        ValueDataEntry("Haha", Random().nextInt()),
                        ValueDataEntry("Hihi", Random().nextInt()),
                        ValueDataEntry("Hoho", Random().nextInt()),
                        ValueDataEntry("Huhu", Random().nextInt())
                    )
                )
            }
        )
    }
}