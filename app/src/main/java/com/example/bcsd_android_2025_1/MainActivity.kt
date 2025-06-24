package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            val permission = getAudioPermission()

            if (isGranted) {
                getAudioFile()
            } else {
                if (shouldShowRequestPermissionRationale(permission)) {
                    showPermissionRationaleDialog()
                } else {
                    showSettingsDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        requestPermission.launch(getAudioPermission())
    }

    private fun getAudioPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.permission_required_music))
            .setPositiveButton(getString(R.string.permission_request)) { _, _ ->
                requestPermission.launch(getAudioPermission())
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.permission_setup_request))
            .setPositiveButton(getString(R.string.permission_move_settings)) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun getAudioFile() {
        val musicList = mutableListOf<Music>()
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, selection, null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val title = cursor.getString(titleCol)
                val artist = cursor.getString(artistCol)
                val duration = cursor.getLong(durationCol)

                musicList.add(Music(title, artist, duration))
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MusicAdapter(musicList)
    }
}
