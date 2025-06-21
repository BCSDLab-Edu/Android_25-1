package com.example.bcsd_android_2025_1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var permissionLayout: View
    private lateinit var btnRequestPermission: Button

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadMusicFiles()
        } else {
            showPermissionLayout()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.musicRecyclerView)
        permissionLayout = findViewById(R.id.permissionLayout)
        btnRequestPermission = findViewById(R.id.btnRequestPermission)

        btnRequestPermission.setOnClickListener {
            requestPermission()
        }

        if (isPermissionGranted()) {
            loadMusicFiles()
        } else {
            showPermissionLayout()
            requestPermission()
        }
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
            val titleIdx = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistIdx = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val durationIdx = it.getColumnIndex(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val title = it.getString(titleIdx) ?: "Unknown"
                val artist = it.getString(artistIdx) ?: "Unknown"
                val duration = it.getLong(durationIdx)
                musicList.add(Music(title, artist, duration))
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MusicAdapter(musicList)
    }
}
