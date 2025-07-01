package com.example.bcsd_android_2025_1.service

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Size
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.example.bcsd_android_2025_1.MainActivity
import com.example.bcsd_android_2025_1.R
import com.example.bcsd_android_2025_1.model.MusicData
import com.example.bcsd_android_2025_1.utils.getAlbumArt
import com.example.bcsd_android_2025_1.utils.getBitmapAlbumArt
import java.io.FileNotFoundException
import java.io.InputStream

class MusicService : Service() {

    companion object {
        const val CHANNEL_ID = "notification_music"
        const val notificationId = 1000
    }

    private val binder = MusicBinder()
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var nowMusic: MusicData
    lateinit var onMediaStateChangeCallback: OnMediaStateChangeCallback

    lateinit var audioFocusRequest: AudioFocusRequest

    private val audioFocusChangeListener =
        OnAudioFocusChangeListener {
            playPauseMusic()
            onMediaStateChangeCallback.onMediaStateChange(false)
        }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()
        audioManager.requestAudioFocus(audioFocusRequest)
        return START_STICKY
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_desc)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            setShowBadge(false)
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(musicData: MusicData) {
        val image: Bitmap = musicData.albumUri.toUri().getBitmapAlbumArt(this, resources)
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_music)
            .setLargeIcon(image)
            .setContentTitle(musicData.title)
            .setContentText(musicData.artist)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Need for api <= 25
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (checkSelfPermission(POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notify(notificationId, builder.build())
            }
        }
        startForeground(notificationId, builder.build())
    }

    fun startMusic(musicData: MusicData) {
        nowMusic = musicData
        if (this::mediaPlayer.isInitialized && isPlaying())
            stopMusic()
        createNotification(musicData)
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            setDataSource(applicationContext, musicData.musicUri.toUri())
            prepareAsync()
        }

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            onMediaStateChangeCallback.mediaPlayStart()
        }

        mediaPlayer.setOnCompletionListener {
            onMediaStateChangeCallback.mediaPlayEnd()
            killService()
        }
        onMediaStateChangeCallback.onMediaStateChange(true)
    }

    private fun stopMusic() {
        mediaPlayer.apply {
            stop()
        }
    }

    fun isPlaying(): Boolean {
        return if (isMediaPlayerInitialized())
            mediaPlayer.isPlaying
        else
            false
    }

    fun isPaused(): Boolean {
        return if (isMediaPlayerInitialized())
            mediaPlayer.currentPosition != 0
        else
            false
    }


    private fun isMediaPlayerInitialized(): Boolean {
        return this::mediaPlayer.isInitialized
    }

    fun playPauseMusic() {
        if (isMediaPlayerInitialized()) {
            when (isPlaying()) {
                true -> {
                    mediaPlayer.pause()
                }

                else -> {
                    mediaPlayer.start()
                }
            }
            onMediaStateChangeCallback.onMediaStateChange(isPlaying())
        }
    }

    fun updatePosition(seconds: Int) {
        mediaPlayer.seekTo(seconds * 1000)
    }

    fun killService() {
        val audioManager = this.getSystemService(AUDIO_SERVICE) as AudioManager

        audioManager.abandonAudioFocusRequest(audioFocusRequest)

        stopForeground(true)
        stopSelf()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun setMediaStateChangeCallback(onMediaStateChangeCallback: OnMediaStateChangeCallback) {
        this.onMediaStateChangeCallback = onMediaStateChangeCallback

    }

    interface OnMediaStateChangeCallback {
        fun onMediaStateChange(isPlaying: Boolean)
        fun mediaPlayEnd()
        fun mediaPlayStart()
    }
}