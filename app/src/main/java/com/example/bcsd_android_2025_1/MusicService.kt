package com.example.bcsd_android_2025_1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    companion object{
        const val CHANNEL_ID = "notification_music"
        const val CHANNEL_NAME = "music_channel"
        const val NOTIFICATION_ID = 1111
        const val TITLE_KEY = "title"
        const val MUSIC_URI_KEY = "musicUri"
        const val MUSIC_TITLE_KEY = "musicTitle"
        const val TITLE_INTENT_ACTION = "MUSIC_PLAYING"
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val musicUriString = intent?.getStringExtra(MUSIC_URI_KEY)
        val title = intent?.getStringExtra(MUSIC_TITLE_KEY) ?: getString(R.string.default_title)

        if (musicUriString != null) {
            val uri = Uri.parse(musicUriString)

            mediaPlayer?.release()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(NOTIFICATION_ID, buildNotification(title), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
            } else {
                startForeground(NOTIFICATION_ID, buildNotification(title))
            }

            mediaPlayer = MediaPlayer().apply {
                setOnPreparedListener {
                    it.start()
                }
                setDataSource(applicationContext, uri)
                prepareAsync()
            }

            val intentTitle = Intent(this, LocalChangeReceiver::class.java)
            intentTitle.action = TITLE_INTENT_ACTION
            intentTitle.putExtra(TITLE_KEY, title)
            sendBroadcast(intentTitle)
        }

        return START_STICKY
    }

    private fun buildNotification(title: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_playing_music))
            .setContentText(title)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}