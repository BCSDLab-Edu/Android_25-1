package com.example.bcsd_android_2025_1

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var permissionName: String
    private lateinit var audioAdapter: AudioAdapter

    private lateinit var tvMain : TextView
    private lateinit var btnMain : Button

    private lateinit var audioListManager : AudioListManager

    private lateinit var playingAudioAdapter : PlayingAudioAdapter

    companion object {
        const val NOW_PLAYING = "nowPlaying"
    }

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

    private val nowPlayingReceive = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val title = p1?.getStringExtra("AUDIO_TITLE") ?: return
            val id = p1.getLongExtra("AUDIO_ID", -1)
            val artist = p1.getStringExtra("AUDIO_ARTIST") ?: "알 수 없음"
            val duration = p1.getLongExtra("AUDIO_DURATION",0)
            val uri = p1.getStringExtra("AUDIO_URI")?.toUri() ?: return

            val item = AudioItem(id, title, artist, duration, uri)
            playingAudioAdapter.setItem(item)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

//초기화
        playingAudioAdapter = PlayingAudioAdapter()
        audioAdapter =AudioAdapter()
        audioListManager = AudioListManager(application)
//리사이클(전체)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_main)
        recyclerView.adapter = audioAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
//리사이클(현재)
        val playingRecyclerView = findViewById<RecyclerView>(R.id.rv_playing)
        playingRecyclerView.adapter = playingAudioAdapter
        playingRecyclerView.layoutManager = LinearLayoutManager(this)

        requestPermission.launch(permissionName)

        tvMain = findViewById(R.id.tv_main)
        btnMain = findViewById(R.id.btn_main)

        audioListManager.audioListObserve.observe(this) {list ->
            audioAdapter.submitList(list)
        }

    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(nowPlayingReceive, IntentFilter(NOW_PLAYING), RECEIVER_NOT_EXPORTED)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(nowPlayingReceive)
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

        }

        tvMain.visibility = View.VISIBLE
        btnMain.visibility = View.VISIBLE

        btnMain.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:$packageName".toUri()
            }
            startActivity(intent)
        }
    }

    private fun getAudioFile() {
        tvMain.visibility = View.GONE
        btnMain.visibility = View.GONE
        audioListManager.loadAudioFiles()
    }

    data class AudioItem(
        val id: Long,
        val name: String,
        val artist: String,
        val duration: Long,
        val uri: Uri
    )


}