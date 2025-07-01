package com.example.bcsd_android_2025_1

import android.app.*
import android.content.*
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.os.Build


class MusicPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentTitle: String = ""
    private var currentArtist: String = ""

    companion object {
        const val CHANNEL_ID = "MusicPlayerChannel"
        const val EXTRA_MUSIC_URI = "music_uri"
        const val EXTRA_TITLE = "music_title"
        const val EXTRA_ARTIST = "music_artist"
    }

    private val commandReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "com.example.bcsd_android_2025_1.ACTION_TOGGLE_PLAY" -> {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.pause()
                    } else {
                        mediaPlayer?.start()
                    }
                    broadcastNowPlaying()
                }
                "com.example.bcsd_android_2025_1.REQUEST_NOW_PLAYING" -> {
                    broadcastNowPlaying()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val filter = IntentFilter().apply {
            addAction("com.example.bcsd_android_2025_1.ACTION_TOGGLE_PLAY")
            addAction("com.example.bcsd_android_2025_1.REQUEST_NOW_PLAYING")
        }
        registerReceiver(commandReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val musicUri = it.getParcelableExtra<Uri>(EXTRA_MUSIC_URI)
            currentTitle = it.getStringExtra(EXTRA_TITLE) ?: "Unknown"
            currentArtist = it.getStringExtra(EXTRA_ARTIST) ?: "Unknown"

            musicUri?.let { uri ->
                playMusic(uri)
            }
        }
        return START_STICKY
    }

    private fun playMusic(uri: Uri) {
        mediaPlayer?.release()
        try {
            val fd = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
            if (fd != null) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(fd)
                    prepare()
                    start()
                }
                showNotification()
                broadcastNowPlaying()
            } else {
                stopSelf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }


    private fun showNotification() {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentTitle)
            .setContentText(currentArtist)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun broadcastNowPlaying() {
        val isPlaying = mediaPlayer?.isPlaying == true
        val intent = Intent("com.example.bcsd_android_2025_1.NOW_PLAYING").apply {
            putExtra("title", currentTitle)
            putExtra("artist", currentArtist)
            putExtra("isPlaying", isPlaying)
        }
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        unregisterReceiver(commandReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "음악 재생 채널",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "음악 재생 상태를 보여주는 채널"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

}
