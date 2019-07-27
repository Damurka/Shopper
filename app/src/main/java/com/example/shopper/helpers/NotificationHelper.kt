package com.example.shopper.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shopper.R

object NotificationHelper {

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, description: String) {
        // Safety checked the OS version for API 26 and greater.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Created a unique name for the notification channel.
            // The name and description are displayed in the application’s Notification settings.
            val appName = context.getString(R.string.app_name)
            val channelId = "${context.packageName}-$appName"
            val channel = NotificationChannel(channelId, appName, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // Created the channel using the NotificationManager.
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, title: String, content: String, autoCancel: Boolean) {
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(title)
            setContentText(content)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }
}