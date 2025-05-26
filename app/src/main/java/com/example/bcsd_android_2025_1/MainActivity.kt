package com.example.bcsd_android_2025_1

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.adapter.MusicAdapter
import com.example.bcsd_android_2025_1.model.MusicData
import com.example.bcsd_android_2025_1.service.MusicService
import com.example.bcsd_android_2025_1.utils.OpenSettings
import com.example.bcsd_android_2025_1.utils.getAlbumArt
import com.example.bcsd_android_2025_1.utils.toDurationFromSecond
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { (permission, isGranted) ->
                if (permission == Manifest.permission.POST_NOTIFICATIONS) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        when (isGranted) {
                            true -> {}
                            else -> {
                                when (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                                    true -> permissionDialog(true)
                                    else -> permissionDialog(false)
                                }
                            }
                        }
                    }
                } else if (
                    permission == Manifest.permission.READ_EXTERNAL_STORAGE || permission == Manifest.permission.READ_MEDIA_AUDIO
                ) {
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
            }
        }

    private val openSettings =
        registerForActivityResult(OpenSettings()) {
            initView()
            hidePermissionSettingsButton()
        }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isSlideUp) {
                    val animSlideDown = TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0f,
                        Animation.RELATIVE_TO_PARENT, 0f,
                        Animation.RELATIVE_TO_PARENT, 0f,
                        Animation.RELATIVE_TO_PARENT, 1f
                    )
                    animSlideDown.duration = 300
                    animSlideDown.fillAfter = false

                    constraintLayoutExpanded.animation = animSlideDown
                    constraintLayoutExpanded.visibility = View.GONE
                    constraintLayoutNowPlaying.visibility = View.VISIBLE
                    isSlideUp = false
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }

    lateinit var musicService: MusicService
    private var isBinding = false
    private var isSlideUp = false
    var job: Job? = null

    private var connectionResult = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isBinding = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBinding = true
            initMusicView()
        }
    }

    private val dataList = mutableListOf<MusicData>()
    private val musicAdapter = MusicAdapter()
    private lateinit var recyclerViewMusicList: RecyclerView
    private lateinit var textViewEmpty: TextView
    private lateinit var textViewPermissionNeeded: TextView
    private lateinit var buttonPermissionSettings: Button
    private lateinit var constraintLayoutNowPlaying: ConstraintLayout
    private lateinit var textViewNowTitle: TextView
    private lateinit var textViewNowArtist: TextView
    private lateinit var imageViewNowAlbumArt: ImageView
    private lateinit var buttonPlayPause: ImageButton
    private lateinit var constraintLayoutExpanded: ConstraintLayout
    private lateinit var textViewExpandedTitle: TextView
    private lateinit var textViewExpandedArtist: TextView
    private lateinit var imageViewExpandedAlbumArt: ImageView
    private lateinit var buttonPlayPauseExpanded: ImageButton
    private lateinit var seekBarExpanded: SeekBar
    private lateinit var textViewExpandedPlayTime: TextView
    private lateinit var textViewExpandedMusicTime: TextView


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        recyclerViewMusicList = findViewById(R.id.recycler_view_music_list)
        textViewEmpty = findViewById(R.id.text_view_empty)
        textViewPermissionNeeded = findViewById(R.id.text_view_permission_needed)
        buttonPermissionSettings = findViewById(R.id.button_permission_settings)

        constraintLayoutNowPlaying = findViewById(R.id.constraint_layout_now_playing)
        imageViewNowAlbumArt = findViewById(R.id.image_view_now_album_art)
        textViewNowTitle = findViewById(R.id.text_view_now_title)
        textViewNowArtist = findViewById(R.id.text_view_now_artist)
        buttonPlayPause = findViewById(R.id.button_play_pause)

        constraintLayoutExpanded = findViewById(R.id.constraint_layout_expanded)
        textViewExpandedTitle = findViewById(R.id.text_view_expanded_title)
        textViewExpandedArtist = findViewById(R.id.text_view_expanded_artist)
        imageViewExpandedAlbumArt = findViewById(R.id.image_view_expanded_album_art)
        buttonPlayPauseExpanded = findViewById(R.id.button_expanded_play_pause)
        seekBarExpanded = findViewById(R.id.seek_bar_expanded)
        textViewExpandedPlayTime = findViewById(R.id.text_view_expanded_play_time)
        textViewExpandedMusicTime = findViewById(R.id.text_view_expanded_music_time)

        val buttonPermissionSettings: Button = findViewById(R.id.button_permission_settings)

        initView()
        when (isBinding) {
            true -> initMusicView()
            else -> initService()
        }

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

        buttonPlayPause.setOnClickListener {
            if (isBinding) {
                if (seekBarExpanded.progress == seekBarExpanded.max) {
                    musicService.startMusic(musicService.nowMusic)
                } else {
                    musicService.playPauseMusic()
                }
            }
        }

        buttonPlayPauseExpanded.setOnClickListener {
            if (isBinding) {
                if (seekBarExpanded.progress == seekBarExpanded.max) {
                    musicService.startMusic(musicService.nowMusic)
                } else {
                    musicService.playPauseMusic()
                }
            }
        }

        musicAdapter.setOnClickListener {
            setNowPlaying(it)
        }

        constraintLayoutNowPlaying.setOnClickListener {
            val animSlideUp = TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0f
            )
            animSlideUp.duration = 300
            animSlideUp.fillAfter = true

            constraintLayoutExpanded.animation = animSlideUp
            constraintLayoutExpanded.visibility = View.VISIBLE

            isSlideUp = true
        }

        constraintLayoutNowPlaying.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            true
        }

        constraintLayoutExpanded.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            true
        }

        constraintLayoutExpanded.setOnClickListener {
            val animSlideDown = TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1f
            )
            animSlideDown.duration = 300
            animSlideDown.fillAfter = false

            constraintLayoutExpanded.animation = animSlideDown
            constraintLayoutExpanded.visibility = View.GONE
            isSlideUp = false
        }

        seekBarExpanded.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    if (fromUser) {
                        Log.d(
                            "Time",
                            "Progress changed: ${progress.toLong().toDurationFromSecond()}"
                        )
                        textViewExpandedPlayTime.text = progress.toLong().toDurationFromSecond()
                        setMusicPosition(progress)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                job?.cancel()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (musicService.isPlaying())
                    waitUntilMusicEnd()
            }
        })
    }

    private fun initService() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
        bindService(intent, connectionResult, Context.BIND_AUTO_CREATE)
    }

    private fun initView() {
        val dividerItemDecoration = DividerItemDecoration(
            recyclerViewMusicList.context,
            LinearLayoutManager(this).orientation
        )

        musicAdapter.dataList = dataList
        recyclerViewMusicList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = musicAdapter
            addItemDecoration(dividerItemDecoration)
        }

        checkPermission()
    }

    private fun initMusicView() {
        if (musicService.isPlaying() || musicService.isPaused()) {
            waitUntilMusicEnd()
            val nowMusic = musicService.nowMusic

            val albumArt = nowMusic.albumUri.toUri().getAlbumArt(this@MainActivity, resources)
            imageViewNowAlbumArt.setImageDrawable(albumArt)
            imageViewExpandedAlbumArt.setImageDrawable(albumArt)

            textViewNowTitle.text = nowMusic.title
            textViewExpandedTitle.text = nowMusic.title
            textViewNowArtist.text = nowMusic.artist
            textViewExpandedArtist.text = nowMusic.artist

            textViewExpandedPlayTime.text = TimeUnit.MILLISECONDS.toSeconds(
                musicService.getCurrentPosition()
                    .toLong()
            ).toDurationFromSecond()
            textViewExpandedMusicTime.text =
                TimeUnit.MILLISECONDS.toSeconds(nowMusic.duration).toDurationFromSecond()
        }

        initCallback()
        initPlayPauseButton(musicService.isPlaying())
    }

    private fun initCallback() {
        val mediaStateChangeCallback = object : MusicService.OnMediaStateChangeCallback {
            override fun onMediaStateChange(isPlaying: Boolean) {
                initPlayPauseButton(isPlaying)
            }

            override fun mediaPlayEnd() {
                initPlayPauseButton(false)
            }

            override fun mediaPlayStart() {
                initMusicView()
                waitUntilMusicEnd()
            }
        }
        musicService.setMediaStateChangeCallback(mediaStateChangeCallback)
    }

    fun initPlayPauseButton(isPlaying: Boolean) {
        when (isPlaying) {
            true -> {
                buttonPlayPause.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_pause
                    )
                )
                buttonPlayPauseExpanded.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_pause
                    )
                )
                waitUntilMusicEnd()
            }

            else -> {
                buttonPlayPause.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_play
                    )
                )
                buttonPlayPauseExpanded.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_play
                    )
                )
                job?.cancel()
            }
        }
    }

    private fun checkPermission() {
        val permissions = arrayOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO
            else Manifest.permission.READ_EXTERNAL_STORAGE,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS
            else ""
        )
        requestPermission.launch(permissions)
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
        constraintLayoutNowPlaying.visibility = View.GONE
        textViewPermissionNeeded.visibility = View.VISIBLE
        buttonPermissionSettings.visibility = View.VISIBLE
    }

    private fun hidePermissionSettingsButton() {
        recyclerViewMusicList.visibility = View.VISIBLE
        constraintLayoutNowPlaying.visibility = View.VISIBLE
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

                val musicUri = "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/$id"

                val albumUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    "${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}/$id"
                } else {
                    "content://media/external/audio/albumart/$albumId"
                }

                dataList.add(MusicData(title, artist, duration, musicUri, albumUri))
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

    private fun setNowPlaying(musicData: MusicData) {
        textViewExpandedMusicTime.text =
            TimeUnit.MILLISECONDS.toSeconds(musicData.duration).toDurationFromSecond()

        when (isBinding) {
            true -> musicService.startMusic(musicData)
            else -> initService()
        }
    }

    private fun setMusicPosition(seconds: Int) {
        musicService.updatePosition(seconds)
    }

    private fun waitUntilMusicEnd() {
        val currentPosition = musicService.getCurrentPosition() / 1000
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(musicService.nowMusic.duration).toInt()

        var nowSeconds = currentPosition
        seekBarExpanded.max = seconds
        seekBarExpanded.progress = currentPosition

        if (job != null && job!!.isActive) {
            job!!.cancel()
        }

        job = CoroutineScope(Dispatchers.Main).launch {
            repeat(seconds - currentPosition) {
                delay(1000)
                nowSeconds++
                seekBarExpanded.incrementProgressBy(1)
                textViewExpandedPlayTime.text = nowSeconds.toLong().toDurationFromSecond()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        if (isBinding)
            if (!musicService.isPlaying()) {
                musicService.killService()
            }
        unbindService(connectionResult)
        isBinding = false
    }

    override fun onBackPressed() {
        if (isSlideUp) {
            val animSlideDown = TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1f
            )
            animSlideDown.duration = 300
            animSlideDown.fillAfter = false

            constraintLayoutExpanded.animation = animSlideDown
            constraintLayoutExpanded.visibility = View.GONE
            constraintLayoutNowPlaying.visibility = View.VISIBLE
            isSlideUp = false
        } else {
            super.onBackPressed()
        }
    }
}