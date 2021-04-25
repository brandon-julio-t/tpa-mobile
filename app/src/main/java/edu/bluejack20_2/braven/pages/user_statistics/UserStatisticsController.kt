package edu.bluejack20_2.braven.pages.user_statistics

import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import edu.bluejack20_2.braven.databinding.FragmentUserStatisticsBinding
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService
import java.time.Month
import java.time.YearMonth
import java.util.*
import javax.inject.Inject

class UserStatisticsController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val timestampService: TimestampService
) {
    lateinit var binding: FragmentUserStatisticsBinding

    fun bind(fragment: UserStatisticsFragment) {
        binding = fragment.binding

        handlePostsInAMonth()
        handlePostsInAYear()
        handlePostCategoryChart()
    }

    private fun handlePostsInAMonth() {
        authenticationService.getUser()?.let { user ->
            val now = Timestamp.now()
            val start = timestampService.minusMonths(now, 1)

            postService.getAllPostsByUserBetweenTimestamp(user.uid, start, now).get()
                .addOnSuccessListener { posts ->
                    val data = posts.documents
                        .groupBy { post ->
                            post.getTimestamp("timestamp")
                                ?.let { timestampService.getDayOfMonth(it) }
                        }
                        .mapValues { it.value.size }
                        .let {
                            val map = it.toMutableMap()

                            for (i in 1..YearMonth.now().lengthOfMonth()) {
                                map[i] = map.getOrDefault(i, 0)
                            }

                            map
                        }
                        .map { BarEntry(it.key?.toFloat() ?: 0f, it.value.toFloat()) }

                    val dataSet = BarDataSet(data, "Posts in a Month")
                    dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

                    binding.postsInAMonthChart.data = BarData(dataSet)
                    binding.postsInAMonthChart.invalidate()
                }
        }
    }

    private fun handlePostsInAYear() {
        authenticationService.getUser()?.let { user ->
            val now = Timestamp.now()
            val start = timestampService.minusYears(now, 1)

            postService.getAllPostsByUserBetweenTimestamp(user.uid, start, now).get()
                .addOnSuccessListener { posts ->
                    val data = posts.documents
                        .groupBy { post ->
                            post.getTimestamp("timestamp")
                                ?.let { timestampService.getMonthValue(it) }
                        }
                        .mapValues { it.value.size }
                        .let {
                            val map = it.toMutableMap()

                            for (i in 1..12) {
                                map[i] = map.getOrDefault(i, 0)
                            }

                            map
                        }
                        .map { BarEntry(it.key?.toFloat() ?: 0f, it.value.toFloat()) }

                    val dataSet = BarDataSet(data, "Posts in a Month")
                    dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

                    binding.postsInAYearChart
                    binding.postsInAYearChart.data = BarData(dataSet)
                    binding.postsInAYearChart.xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                            return Month.of(value.toInt()).toString()
                        }
                    }
                    binding.postsInAYearChart.invalidate()
                }
        }
    }

    private fun handlePostCategoryChart() {
        val categoryData = mutableMapOf<String, Float>()

        authenticationService.getUser()?.let { user ->
            postService.getAllPostsByUser(user.uid).get().addOnSuccessListener { posts ->
                posts.documents.forEach { post ->
                    post.getString("category")?.let { category ->
                        categoryData[category] = (categoryData[category] ?: 0f) + 1f
                    }
                }

                val total = categoryData.values.sum()
                val entries = categoryData.map { (_, count) ->
                    PieEntry(count, "%.2f".format(count / total * 100f) + "%")
                }

                val dataSet = PieDataSet(entries, "Post Categories")
                dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

                binding.postCategoriesChart
                binding.postCategoriesChart.data = PieData(dataSet)
                binding.postCategoriesChart.data.setValueFormatter(PercentFormatter())
                binding.postCategoriesChart.invalidate()
            }
        }
    }
}