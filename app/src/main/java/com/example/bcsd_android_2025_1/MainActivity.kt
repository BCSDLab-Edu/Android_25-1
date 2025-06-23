package com.example.bcsd_android_2025_1
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.provider.MediaStore

class MainActivity : AppCompatActivity() {

    private lateinit var settingsBtn: Button
    private lateinit var explanationText: TextView

    private val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showMusicList()
            } else {
                if (shouldShowRequestPermissionRationale(permission)) {
                    showPermissionDialog()
                } else {
                    showSettingsButton()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsBtn = findViewById(R.id.button_goto_settings)
        explanationText = findViewById(R.id.textview_permission)

        settingsBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }

        checkPermissionAndRequest()
    }

    private fun checkPermissionAndRequest() {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            showMusicList()
        } else {
            requestPermission.launch(permission)
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permdialog_title)
            .setMessage(R.string.permdialog_title)
            .setPositiveButton(R.string.permdialog_pos) { _, _ ->
                requestPermission.launch(permission)
            }
            .setNegativeButton(R.string.permdialog_neg, null)
            .show()
    }

    private fun showSettingsButton() {
        explanationText.visibility = View.VISIBLE
        settingsBtn.visibility = View.VISIBLE
    }

    private fun showMusicList() {
        val musicList = mutableListOf<Triple<String, String, Long>>()

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )
        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val timeIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex) ?: "제목없음"
                val artist = it.getString(artistIndex) ?: "가수없음"
                val time = it.getLong(timeIndex)
                musicList.add(Triple(title, artist, time))
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = MusicAdapter(musicList)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}