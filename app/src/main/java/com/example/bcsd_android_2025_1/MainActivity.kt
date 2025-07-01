package com.example.bcsd_android_2025_1

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var permissionLayout: View
    private lateinit var btnRequestPermission: Button
    private lateinit var playerLayout: View
    private lateinit var currentTitle: TextView
    private lateinit var currentArtist: TextView
    private lateinit var btnPlayPause: ImageButton
    private var permissionDeniedCount = 0


    private val nowPlayingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra("title") ?: return
            val artist = intent.getStringExtra("artist") ?: return
            val isPlaying = intent.getBooleanExtra("isPlaying", false)

            currentTitle.text = title
            currentArtist.text = artist
            btnPlayPause.setImageResource(
                if (isPlaying) android.R.drawable.ic_media_pause
                else android.R.drawable.ic_media_play
            )
            playerLayout.visibility = View.VISIBLE
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadMusicFiles()
        } else {
            permissionDeniedCount++
            if (permissionDeniedCount < 2) {
                requestPermission()
            } else {
                showPermissionLayout()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        recyclerView = findViewById(R.id.music_recycler_view)
        permissionLayout = findViewById(R.id.permission_layout)
        btnRequestPermission = findViewById(R.id.btn_request_permission)
        playerLayout = findViewById(R.id.player_layout)
        currentTitle = findViewById(R.id.text_current_title)
        currentArtist = findViewById(R.id.text_current_artist)
        btnPlayPause = findViewById(R.id.button_play_pause)

        btnRequestPermission.setOnClickListener {
            if (permissionDeniedCount < 2) {
                requestPermission()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
        }

        btnPlayPause.setOnClickListener {
            val intent = Intent("com.example.bcsd_android_2025_1.ACTION_TOGGLE_PLAY")
            sendBroadcast(intent)
        }

        if (isPermissionGranted()) {
            loadMusicFiles()
        } else {
            showPermissionLayout()
            requestPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            nowPlayingReceiver,
            IntentFilter("com.example.bcsd_android_2025_1.NOW_PLAYING"),
            RECEIVER_NOT_EXPORTED
        )

        sendBroadcast(Intent("com.example.bcsd_android_2025_1.REQUEST_NOW_PLAYING"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(nowPlayingReceiver)
    }

    private fun isPermissionGranted(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        permissionLauncher.launch(permission)
    }

    private fun showPermissionLayout() {
        recyclerView.visibility = View.GONE
        permissionLayout.visibility = View.VISIBLE
    }

    private fun loadMusicFiles() {
        recyclerView.visibility = View.VISIBLE
        permissionLayout.visibility = View.GONE

        val musicList = mutableListOf<Music>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = contentResolver.query(
            uri,
            projection,
            MediaStore.Audio.Media.IS_MUSIC + "!= 0",
            null,
            null
        )

        cursor?.use {
            val idIdx = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIdx = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIdx = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationIdx = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idIdx)
                val contentUri = ContentUris.withAppendedId(uri, id)
                val title = it.getString(titleIdx) ?: "Unknown"
                val artist = it.getString(artistIdx) ?: "Unknown"
                val duration = it.getLong(durationIdx)
                musicList.add(Music(title, artist, duration, contentUri))
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MusicAdapter(musicList) { selectedMusic ->
            val serviceIntent = Intent(this, MusicPlayerService::class.java).apply {
                putExtra(MusicPlayerService.EXTRA_MUSIC_URI, selectedMusic.uri)
                putExtra(MusicPlayerService.EXTRA_TITLE, selectedMusic.title)
                putExtra(MusicPlayerService.EXTRA_ARTIST, selectedMusic.artist)
            }
            startForegroundService(serviceIntent)
        }
    }
}
