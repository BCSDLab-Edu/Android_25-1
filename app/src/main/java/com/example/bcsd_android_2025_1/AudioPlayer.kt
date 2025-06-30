package com.example.bcsd_android_2025_1

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


@Suppress("DEPRECATION")
class AudioPlayer : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentAudioTitle: String = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val audioUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("AUDIO_URI", Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra("AUDIO_URI")

        }
        currentAudioTitle = intent?.getStringExtra("AUDIO_TITLE") ?: "음악"

        audioUri?.let {
            mediaPlayer = MediaPlayer.create(this, audioUri)
            mediaPlayer.setOnCompletionListener {
                stopForeground(true)
            }
            mediaPlayer.start()
            showNotification(currentAudioTitle)
            nowPlaying(currentAudioTitle)
        }
        return START_NOT_STICKY
    }

    private fun showNotification(title: String) {
        val channelId = "music_channel"
        val channelName = "음악 재생 채널"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )



        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("재생 중")
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        if (::mediaPlayer.isInitialized){
            mediaPlayer.release()
        }
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun nowPlaying(title: String) {
        val intent = Intent(MainActivity.NOW_PLAYING)
        intent.putExtra("AUDIO_TITLE",title)
        sendBroadcast(intent)
    }

}