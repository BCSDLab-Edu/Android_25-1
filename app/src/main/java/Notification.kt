package com.example.bcsd_android_2025_1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class Notification(private val context: Context) {


    private var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
            createNotificationChannel()
    }

    private fun createNotificationChannel() {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                    .apply {
                        description = "description"
                    }
            notificationManager.createNotificationChannel(notificationChannel)
    }

    fun deliverNotification(bundle: Bundle) {
        val plusIntent = Intent(context, SecondActivity::class.java).apply{
            putExtras(bundle)
        }
        val plusPendingIntent = PendingIntent.getActivity(
            context, NOTIFICATION_ID, plusIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_CANCEL_NOTIFICATION"
        }

        val closePendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID + 1,
            closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val legend = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_photo)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_photo)
            .setContentTitle(context.getString(R.string.text_title))
            .setContentText(context.getString(R.string.text_context))
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(legend)
            )
            .setLargeIcon(legend)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, context.getString(R.string.text_notification_plus), plusPendingIntent)
            .addAction(0,context.getString(R.string.text_notification_close),closePendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
    companion object {
        const val CHANNEL_ID = "1011"
        const val CHANNEL_NAME = "chanyoung"
        const val NOTIFICATION_ID = 0
    }

}