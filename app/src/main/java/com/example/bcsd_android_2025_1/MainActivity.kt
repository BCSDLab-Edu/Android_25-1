package com.example.bcsd_android_2025_1

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding
import java.io.File

data class MusicItem(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: String,
    val albumId: Long,
    val data: String
)

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentlyPlaying: MusicItem? = null

    private lateinit var binding: ActivityMainBinding

    private lateinit var musicAdapter: MusicAdapter
    private val musicList = mutableListOf<MusicItem>()

    private val musicPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadMusicFiles()
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            showPermissionDeniedDialog()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        initRecyclerView()
        checkAndRequestPermission()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, musicPermission) == PackageManager.PERMISSION_GRANTED && musicList.isEmpty()) {
            loadMusicFiles()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    @SuppressLint("MissingPermission")
    private fun initRecyclerView() {
        musicAdapter = MusicAdapter(musicList) { clickedItem ->

            if (currentlyPlaying?.id == clickedItem.id && mediaPlayer?.isPlaying == true) {
                pauseMusic()
            } else {
                playMusic(clickedItem)
            }
        }

        binding.recyclerViewMusic.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = musicAdapter
            setHasFixedSize(true)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun playMusic(item: MusicItem) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(item.data)
            prepare()
            start()
            setOnCompletionListener {
                Toast.makeText(
                    this@MainActivity,
                    "\"${item.title}\" music end.",
                    Toast.LENGTH_SHORT
                ).show()
                currentlyPlaying = null
            }
        }
        currentlyPlaying = item
        Toast.makeText(this, "\"${item.title}\" playing now", Toast.LENGTH_SHORT).show()
        showNowPlayingNotification(item)
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        Toast.makeText(this, "일시정지", Toast.LENGTH_SHORT).show()
    }


    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, musicPermission) == PackageManager.PERMISSION_GRANTED) {
            loadMusicFiles()
        } else {
            showPermissionRequestDialog()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "음악 재생 상태 표시" }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNowPlayingNotification(item: MusicItem) {
        val notification = NotificationCompat.Builder(this, "music_channel")
            .setSmallIcon(R.drawable.ic_media_play)
            .setContentTitle(item.title)
            .setContentText("artist: ${item.artist}")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }

    private fun showPermissionRequestDialog() {
        AlertDialog.Builder(this)
            .setTitle("음악 및 오디오 접근 권한")
            .setMessage("음악 파일을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("허용") { _, _ ->
                requestPermissionLauncher.launch(musicPermission)
            }
            .setNegativeButton("거부") { _, _ ->
                showPermissionDeniedDialog()
            }
            .setCancelable(false)
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("음악 파일을 표시하려면 권한이 필요합니다.\n\n설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정 열기") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("종료") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun loadMusicFiles() {
        val file = File("/sdcard/Music/Lil_Tecca_Dark_Thoughts.mp3", "/sdcard/Music/Frank_Ocean_Pink_+_White.mp3")

        if (file.exists()) {
            MediaScannerConnection.scanFile(this, arrayOf(file.absolutePath), null, null)
        }

        musicList.clear()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn) ?: "Unknown Title"
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val duration = formatDuration(it.getLong(durationColumn))
                val albumId = it.getLong(albumIdColumn)
                val data = it.getString(dataColumn) ?: ""

                musicList.add(MusicItem(id, title, artist, duration, albumId, data))
            }
        }

        musicAdapter.notifyDataSetChanged()
    }

    private fun formatDuration(durationMs: Long): String {
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}
