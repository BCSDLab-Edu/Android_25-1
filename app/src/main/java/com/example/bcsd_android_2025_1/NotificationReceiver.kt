package com.example.bcsd_android_2025_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_CLOSE_NOTIFICATION") {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(1001)
        }
    }
}