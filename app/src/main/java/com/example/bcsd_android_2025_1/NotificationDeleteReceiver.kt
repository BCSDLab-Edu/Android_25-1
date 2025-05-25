package com.example.bcsd_android_2025_1

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationDeleteReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (intent.hasExtra(MainActivity.KEY_NOTIFICATION)) {
            val notificationId = intent.getIntExtra(MainActivity.KEY_NOTIFICATION, 0)
            notificationManager.cancel(notificationId)
        }
    }
}