package edu.bluejack20_2.braven.services

import com.google.firebase.Timestamp
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*
import javax.inject.Inject

class TimestampService @Inject constructor() {
    private val zoneId get() = TimeZone.getDefault().toZoneId()
    private val locale get() = Locale.getDefault()

    fun prettyTime(timestamp: Timestamp) = PrettyTime().format(timestamp.toDate()).toString()

    fun formatMilliseconds(ms: Long, format: String): String {
        val calendar = Calendar.getInstance().also {
            it.timeInMillis = ms
        }
        return SimpleDateFormat(format, locale).format(calendar.time)
    }

    fun formatTimestamp(timestamp: Timestamp, format: String) =
        SimpleDateFormat(format, locale).format(timestamp.toDate())

    fun millisecondsToTimestamp(ms: Long) = Timestamp(Date(ms))

    fun minusWeeks(timestamp: Timestamp, weeks: Long): Timestamp =
        processTimestamp(timestamp, LocalDateTime::minusWeeks, weeks)

    private fun processTimestamp(
        timestamp: Timestamp,
        process: (LocalDateTime, Long) -> LocalDateTime,
        interval: Long
    ): Timestamp {
        val timestampInstant = timestamp.toDate().toInstant()
        val timestampLocalDateTime = LocalDateTime.ofInstant(timestampInstant, zoneId)
        val timestampProcessed = process(timestampLocalDateTime, interval)
        val timestampDateTimeOffset = OffsetDateTime.ofInstant(timestampInstant, zoneId).offset
        val timestampInstantResult = timestampProcessed.toInstant(timestampDateTimeOffset)
        val timestampProcessedDate = Date.from(timestampInstantResult)
        return Timestamp(timestampProcessedDate)
    }

    fun minusMonths(timestamp: Timestamp, months: Long) =
        processTimestamp(timestamp, LocalDateTime::minusMonths, months)

    fun minusYears(timestamp: Timestamp, years: Long) =
        processTimestamp(timestamp, LocalDateTime::minusYears, years)

    fun getDayOfWeek(timestamp: Timestamp) =
        LocalDateTime.ofInstant(timestamp.toDate().toInstant(), zoneId).dayOfWeek.value

    fun getDayOfMonth(timestamp: Timestamp) =
        LocalDateTime.ofInstant(timestamp.toDate().toInstant(), zoneId).dayOfMonth

    fun getMonthValue(timestamp: Timestamp) =
        LocalDateTime.ofInstant(timestamp.toDate().toInstant(), zoneId).month.value

    companion object FORMATS {
        const val PRETTY_SHORT = "d MMM y"
        const val PRETTY_LONG = "d MMMM y"
    }
}