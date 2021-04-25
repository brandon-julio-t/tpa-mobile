package edu.bluejack20_2.braven.services

import com.google.firebase.Timestamp
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Inject

class TimestampService @Inject constructor() {
    fun prettyTime(timestamp: Timestamp) = PrettyTime().format(timestamp.toDate()).toString()
}