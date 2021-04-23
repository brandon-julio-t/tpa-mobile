package edu.bluejack20_2.braven.pages.user_statistics

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import edu.bluejack20_2.braven.databinding.FragmentUserStatisticsBinding
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.services.AuthenticationService
import java.time.LocalDateTime
import java.time.Month
import java.time.OffsetDateTime
import java.util.*
import javax.inject.Inject

class UserStatisticsController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService
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
            val nowInstant = now.toDate().toInstant()
            val zoneId = TimeZone.getDefault().toZoneId()

            val start = LocalDateTime.ofInstant(nowInstant, zoneId)
                .minusMonths(1)
                .toInstant(OffsetDateTime.ofInstant(nowInstant, zoneId).offset).let {
                    Timestamp(Date.from(it))
                }

            postService.getAllPostsByUserBetweenTimestamp(user.uid, start, now).get()
                .addOnSuccessListener { posts ->
                    val data = posts.documents
                        .groupBy { post ->
                            post.getTimestamp("timestamp")?.let {
                                LocalDateTime.ofInstant(
                                    it.toDate().toInstant(),
                                    zoneId
                                ).dayOfMonth
                            }
                        }
                        .mapValues { it.value.size }
                        .map { BarEntry(it.key?.toFloat() ?: 0f, it.value.toFloat()) }

                    val dataSet = BarDataSet(data, "Posts in a Month").also {
                        it.colors = ColorTemplate.COLORFUL_COLORS.toList()
                    }

                    binding.postsInAMonthChart.let {
                        it.data = BarData(dataSet)
                        it.invalidate()
                    }
                }
        }
    }

    private fun handlePostsInAYear() {
        authenticationService.getUser()?.let { user ->
            val now = Timestamp.now()
            val nowInstant = now.toDate().toInstant()
            val zoneId = TimeZone.getDefault().toZoneId()

            val start = LocalDateTime.ofInstant(nowInstant, zoneId)
                .minusYears(1)
                .toInstant(OffsetDateTime.ofInstant(nowInstant, zoneId).offset).let {
                    Timestamp(Date.from(it))
                }

            postService.getAllPostsByUserBetweenTimestamp(user.uid, start, now).get()
                .addOnSuccessListener { posts ->
                    val data = posts.documents
                        .groupBy { post ->
                            post.getTimestamp("timestamp")?.let {
                                LocalDateTime.ofInstant(
                                    it.toDate().toInstant(),
                                    zoneId
                                ).monthValue
                            }
                        }
                        .mapValues { it.value.size }
                        .map { BarEntry(it.key?.toFloat() ?: 0f, it.value.toFloat()) }

                    val dataSet = BarDataSet(data, "Posts in a Month").also {
                        it.colors = ColorTemplate.COLORFUL_COLORS.toList()
                    }

                    binding.postsInAYearChart.let {
                        it.data = BarData(dataSet)
                        it.xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                                return Month.of(value.toInt()).toString()
                            }
                        }
                        it.invalidate()
                    }
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

                val dataSet = PieDataSet(entries, "Post Categories").also {
                    it.colors = ColorTemplate.COLORFUL_COLORS.toList()
                }

                binding.postCategoriesChart.let {
                    it.data = PieData(dataSet).apply { setValueFormatter(PercentFormatter()) }
                    it.invalidate()
                }
            }
        }
    }
}