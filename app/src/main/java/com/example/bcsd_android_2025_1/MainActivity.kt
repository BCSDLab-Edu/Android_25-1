package com.example.bcsd_android_2025_1
import android.Manifest
import android.content.BroadcastReceiver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.BatteryManager
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
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.app.ActivityCompat
import android.widget.Toast
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var settingsBtn: Button
    private lateinit var explanationText: TextView
    private lateinit var nowsongText: TextView

    private val musicUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra(MusicPlayerService.SONG_TITLE) ?: return
            Log.d("MainActivity", "Music update received: $title")
            findViewById<TextView>(R.id.textview_nowsong).text = title
        }
    }

    private val batteryReceiver = object  : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

            if (level >= 0 && scale > 0) {
                val batterysize = level * 100 / scale
                if (batterysize <= 15) {
                        Toast.makeText(context, R.string.battery_msg, Toast.LENGTH_SHORT).show()
                        stopService(Intent(context, MusicPlayerService::class.java))
                }
            }
        }
    }

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
        nowsongText = findViewById(R.id.textview_nowsong)

        settingsBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }
        requestNotificationPermission()
        checkPermissionAndRequest()

        val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, batteryFilter)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(MusicPlayerService.MUSIC_UPDATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                musicUpdateReceiver,
                filter,
                null,
                null,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(musicUpdateReceiver, filter)
        }
    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(musicUpdateReceiver)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        unregisterReceiver(musicUpdateReceiver)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    2001
                )
            }
        }
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
            .setMessage(R.string.permdialog_msg)
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
        lifecycleScope.launch {
            val musicList = withContext(Dispatchers.IO) {
                loadMusicFromMediaStore()
            }
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = MusicAdapter(musicList) { musicInfo ->
                val intent = Intent(this@MainActivity, MusicPlayerService::class.java).apply {
                    putExtra(MusicPlayerService.SONG_TITLE, musicInfo.title)
                    putExtra(MusicPlayerService.SONG_URI, musicInfo.uri.toString())
                }
                ContextCompat.startForegroundService(this@MainActivity, intent)
            }
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    data class MusicInfo(
        val title: String,
        val artist: String,
        val time: Long,
        val uri: Uri
    )

    private fun loadMusicFromMediaStore(): List<MusicInfo> {
        val musicList = mutableListOf<MusicInfo>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
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
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val timeIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex) ?: "제목없음"
                val artist = it.getString(artistIndex) ?: "가수없음"
                val time = it.getLong(timeIndex)
                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                musicList.add(MusicInfo(title, artist, time, contentUri))
            }
        }
        return musicList
    }
}

