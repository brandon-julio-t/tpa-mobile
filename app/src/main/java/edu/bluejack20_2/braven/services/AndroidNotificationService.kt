package edu.bluejack20_2.braven.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.bluejack20_2.braven.R

class AndroidNotificationService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return

        val builder =
            NotificationCompat.Builder(context, "GLOBALS.NOTIFICATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("BRaVeN")
                .setContentText("Continue your journey at BRaVeN!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)

        val channel = NotificationChannel(
            "GLOBALS.NOTIFICATION_CHANNEL_ID",
            "BRaVeN Notification Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, builder.build())
    }
}