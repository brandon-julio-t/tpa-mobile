package edu.bluejack20_2.braven.services

import com.google.firebase.Timestamp
import org.ocpsoft.prettytime.PrettyTime
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*
import javax.inject.Inject

class TimestampService @Inject constructor() {
    private val zoneId get() = TimeZone.getDefault().toZoneId()

    fun prettyTime(timestamp: Timestamp) = PrettyTime().format(timestamp.toDate()).toString()

    fun minusMonths(timestamp: Timestamp, months: Long): Timestamp {
        val timestampInstant = timestamp.toDate().toInstant()

        val processedInstant = LocalDateTime.ofInstant(timestampInstant, zoneId)
            .minusMonths(months)
            .toInstant(OffsetDateTime.ofInstant(timestampInstant, zoneId).offset)

        return Timestamp(Date.from(processedInstant))
    }

    fun minusYears(timestamp: Timestamp, years: Long): Timestamp {
        val timestampInstant = timestamp.toDate().toInstant()

        val processedInstant = LocalDateTime.ofInstant(timestampInstant, zoneId)
            .minusYears(years)
            .toInstant(OffsetDateTime.ofInstant(timestampInstant, zoneId).offset)

        return Timestamp(Date.from(processedInstant))
    }

    fun getDayOfMonth(timestamp: Timestamp) =
        LocalDateTime.ofInstant(timestamp.toDate().toInstant(), zoneId).dayOfMonth

    fun getMonthValue(timestamp: Timestamp) =
        LocalDateTime.ofInstant(timestamp.toDate().toInstant(), zoneId).monthValue
}