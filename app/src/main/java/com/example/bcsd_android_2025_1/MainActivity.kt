package com.example.bcsd_android_2025_1

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.adapter.MusicAdapter
import com.example.bcsd_android_2025_1.model.MusicData
import com.example.bcsd_android_2025_1.utils.OpenSettings

class MainActivity : AppCompatActivity() {
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> getAudioFile()
                else -> {
                    when (
                        shouldShowRequestPermissionRationale(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO
                            else Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        true -> permissionDialog(true)
                        else -> permissionDialog(false)
                    }
                }
            }
        }

    private val openSettings =
        registerForActivityResult(OpenSettings()) {
            initView()
            hidePermissionSettingsButton()
        }

    private val dataList = mutableListOf<MusicData>()
    private val musicAdapter = MusicAdapter()
    private lateinit var recyclerViewMusicList: RecyclerView
    private lateinit var textViewEmpty: TextView
    private lateinit var textViewPermissionNeeded: TextView
    private lateinit var buttonPermissionSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewMusicList = findViewById(R.id.recycler_view_music_list)
        textViewEmpty = findViewById(R.id.text_view_empty)
        textViewPermissionNeeded = findViewById(R.id.text_view_permission_needed)
        buttonPermissionSettings = findViewById(R.id.button_permission_settings)
        val buttonPermissionSettings: Button = findViewById(R.id.button_permission_settings)

        initView()

        buttonPermissionSettings.setOnClickListener {
            /*
            val intent = Intent()
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivity(intent)
             */
            openSettings.launch(Unit)
        }
    }

    private fun checkPermission() {
        requestPermission.launch(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO
            else Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun permissionDialog(isDeniedOnce: Boolean) {
        when (isDeniedOnce) {
            true -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_permission_title))
                    .setMessage(getString(R.string.dialog_permission_messsage))
                    .setPositiveButton(getString(R.string.dialog_permission_ok)) { _, _ ->
                        checkPermission()
                    }
                    .setNegativeButton(getString(R.string.dialog_permission_cancel)) { dialog, _ ->
                        dialog.dismiss()
                        showPermissionSettingsButton()
                    }
                    .setCancelable(false)
                builder.show()
            }

            else -> showPermissionSettingsButton()
        }
    }

    private fun showPermissionSettingsButton() {
        recyclerViewMusicList.visibility = View.GONE
        textViewEmpty.visibility = View.GONE
        textViewPermissionNeeded.visibility = View.VISIBLE
        buttonPermissionSettings.visibility = View.VISIBLE
    }

    private fun hidePermissionSettingsButton() {
        recyclerViewMusicList.visibility = View.VISIBLE
        textViewPermissionNeeded.visibility = View.GONE
        buttonPermissionSettings.visibility = View.GONE
    }

    private fun getAudioFile() {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val sortOrder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            "${MediaStore.Files.FileColumns.ALBUM}, ${MediaStore.Files.FileColumns.ARTIST}, CAST(${MediaStore.Files.FileColumns.CD_TRACK_NUMBER} AS INTEGER)"
        } else {
            " ${MediaStore.Audio.AlbumColumns.ALBUM}, ${MediaStore.Audio.AlbumColumns.ARTIST}, CAST(${MediaStore.Audio.AudioColumns.TRACK} AS INTEGER)"
        }
        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Audio.Media.IS_MUSIC,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)

                val albumUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/$id"
                } else {
                    "content://media/external/audio/albumart/$albumId"
                }

                dataList.add(MusicData(title, artist, duration, albumUri))
                musicAdapter.notifyItemInserted(musicAdapter.itemCount)
            }
        }
        checkIsMusicEmpty()
    }

    private fun checkIsMusicEmpty() {
        if (musicAdapter.itemCount != 0) {
            textViewEmpty.visibility = View.GONE
        } else {
            textViewEmpty.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        val dividerItemDecoration = DividerItemDecoration(
            recyclerViewMusicList.context,
            LinearLayoutManager(this).orientation
        )

        musicAdapter.dataList = dataList

        musicAdapter.setOnClickListener {

        }

        recyclerViewMusicList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = musicAdapter
            addItemDecoration(dividerItemDecoration)
        }

        checkPermission()
    }
}
