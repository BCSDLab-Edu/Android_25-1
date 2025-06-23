package com.example.bcsd_android_2025_1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.model.MusicData

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var permissionMessage: TextView
    private lateinit var openSettingsButton: Button
    private lateinit var musicAdapter: MusicAdapter

    private val musicList = mutableListOf<MusicData>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showMusicList()
        } else {
            showPermissionDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.MusicRecyclerView)
        permissionMessage = findViewById(R.id.text1)
        openSettingsButton = findViewById(R.id.OpenButton)

        musicAdapter = MusicAdapter(musicList)
        recyclerView.adapter = musicAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.visibility = View.GONE
        permissionMessage.visibility = View.GONE
        openSettingsButton.visibility = View.GONE

        if (hasPermission()) {
            showMusicList()
        } else {
            requestPermission()
        }

        openSettingsButton.setOnClickListener {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun hasPermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                showMusicList()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionDialog()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_dialog_title))
            .setMessage(getString(R.string.permission_dialog_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                showPermissionUI()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showPermissionUI() {
        recyclerView.visibility = View.GONE
        permissionMessage.visibility = View.VISIBLE
        openSettingsButton.visibility = View.VISIBLE
    }

    private fun showMusicList() {
        recyclerView.visibility = View.VISIBLE
        permissionMessage.visibility = View.GONE
        openSettingsButton.visibility = View.GONE

        loadMusicList()
    }

    private fun loadMusicList() {
        musicList.clear()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val title = it.getString(0)
                val artist = it.getString(1)
                val duration = it.getLong(2)
                musicList.add(MusicData(title, artist, duration))
            }
        }

        musicAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if (hasPermission()) {
            showMusicList()
        }
    }
}
