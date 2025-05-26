package com.example.bcsd_android_2025_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "ACTION_CANCEL_NOTIFICATION") {
            NotificationManagerCompat.from(context).cancel(Notification.NOTIFICATION_ID)
        }
    }
}