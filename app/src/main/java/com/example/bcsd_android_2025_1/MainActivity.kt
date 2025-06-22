package com.example.bcsd_android_2025_1

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var permissionName: String

    private lateinit var audioAdapter: AudioAdapter

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> getAudioFile()
                else -> {
                    when (shouldShowRequestPermissionRationale(permissionName)) {
                        true -> permissionDialog(true)
                        else -> permissionDialog(false)
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        audioAdapter =AudioAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.rv_main)
        recyclerView.adapter = audioAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        requestPermission.launch(permissionName)

    }

    private fun permissionDialog(isShowRationale: Boolean) {
        if (isShowRationale) {
            AlertDialog.Builder(this)
                .setTitle(R.string.tv_main)
                .setPositiveButton(R.string.alert_btn_positive){
                    _, _ ->
                    requestPermission.launch(permissionName)
                }
                .setNegativeButton(R.string.alert_btn_negative, null)
                .show()
            return
        }
        val tvMain = findViewById<TextView>(R.id.tv_main)
        val btnMain = findViewById<Button>(R.id.btn_main)

        tvMain.visibility = View.VISIBLE
        btnMain.visibility = View.VISIBLE

        btnMain.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:$packageName".toUri()
            }
            startActivity(intent)
        }
    }


    data class AudioItem(
        val id: Long,
        val name: String,
        val artist: String,
        val duration: Long,
        val uri: Uri
    )

    private val audioList = mutableListOf<AudioItem>()


    private fun getAudioFile() {

        findViewById<TextView>(R.id.tv_main).visibility = View.GONE
        findViewById<Button>(R.id.btn_main).visibility = View.GONE

        val projection = arrayOf(
            Media._ID,
            Media.DISPLAY_NAME,
            Media.ARTIST,
            Media.DURATION
        )

        val selection: String? = null
        val selectionArgs: Array<String>? = null

        val sortOrder = "${Media.DATE_ADDED} DESC"

        contentResolver.query(
            Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME)
            val artistColumn = cursor.getColumnIndexOrThrow(Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val artists = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)
                val uri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI,id)

                val item = AudioItem(id, name, artists, duration, uri)
                audioList.add(item)

                audioAdapter.submitList(audioList.toList())

            }
        }


    }


}