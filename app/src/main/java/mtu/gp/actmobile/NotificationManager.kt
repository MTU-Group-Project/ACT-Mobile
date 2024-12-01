package mtu.gp.actmobile

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "mtu.gp.actmobile"

fun showNotification(context: Context) {
    var builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle("Test")
        .setContentText("Test")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
    // TODO: Set Intent

    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance)

    // Register the channel with the system.
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

    notificationManager.notify(0, builder)
}
