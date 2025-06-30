package com.example.bcsd_android_2025_1
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.content.pm.ServiceInfo
import android.util.Log

class MusicPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID = "music_playback_channel"

    companion object {
        const val MUSIC_UPDATE = "com.example.bcsd_android_2025_1.MUSIC_UPDATE"
        const val SONG_TITLE = "SONG_TITLE"
        const val SONG_URI = "SONG_URI"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val songUriString = intent?.getStringExtra(SONG_URI) ?: return START_NOT_STICKY
        val songTitle = intent.getStringExtra(SONG_TITLE) ?: "Unknown"
        val songUri = Uri.parse(songUriString)

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener{
                start()
                Log.d("MusicPlayerService", "Starting playback of: $songTitle")
                val updateIntent = Intent(MUSIC_UPDATE)
                updateIntent.setPackage(packageName)
                updateIntent.putExtra(SONG_TITLE, songTitle)
                sendBroadcast(updateIntent)
            }
            setOnCompletionListener {
                stopSelf()
            }
            setOnErrorListener { _, _, _ ->
                stopSelf()
                true
            }
        }

        try {
            mediaPlayer?.setDataSource(applicationContext, songUri)
            val notification = buildNotification(songTitle)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    1,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(1, notification)
            }

            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    private fun buildNotification(songTitle: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            else
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("현재 재생 음악")
            .setContentText(songTitle)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null
}