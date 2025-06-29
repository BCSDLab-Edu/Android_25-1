package com.example.bcsd_android_2025_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LocalChangeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra(MusicService.TITLE_KEY) ?: R.string.default_title

        val toMainIntent = Intent(MainActivity.MAIN_RECEIVER_ACTION)
        toMainIntent.putExtra(MusicService.TITLE_KEY, title)
        toMainIntent.setPackage("com.example.bcsd_android_2025_1")
        context?.sendBroadcast(toMainIntent)
    }
}