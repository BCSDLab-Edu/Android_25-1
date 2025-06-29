package com.example.bcsd_android_2025_1

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    companion object {
        const val BATTERY_LEVEL = "level"
        const val BATTERY_SCALE = "scale"
        const val MAIN_RECEIVER_ACTION = "action"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var permissionTextView: TextView
    private lateinit var permissionButton: Button
    private lateinit var serviceIntent : Intent
    private lateinit var playingMusicTextView: TextView

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        when(isGranted) {
            true->{
                recyclerView.visibility = View.VISIBLE
                permissionButton.visibility = View.GONE
                permissionTextView.visibility = View.GONE
                loadMusics()
            }
            else->{
                when(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    true->permissionDialog(true)
                    else->{
                        permissionDialog(false)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        serviceIntent = Intent(this,MusicService::class.java)///////

        recyclerView = findViewById(R.id.recycler_view)
        permissionTextView = findViewById(R.id.need_permission_textview)
        permissionButton = findViewById(R.id.need_permission_button)
        playingMusicTextView = findViewById(R.id.playing_music_textview)

        recyclerView.visibility = View.GONE
        permissionButton.visibility = View.GONE
        permissionTextView.visibility = View.GONE

        requestPermissionLaunch()

        permissionButton.setOnClickListener{
            val intent = Intent()
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

    private val mainReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra(MusicService.TITLE_KEY) ?: R.string.default_title
            playingMusicTextView.text = getString(R.string.playing_music_view, title)
        }
    }

    override fun onResume() {
        super.onResume()

        val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryCheckReceiver, batteryFilter)

        val intentfilter = IntentFilter(MAIN_RECEIVER_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mainReceiver, intentfilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(mainReceiver, intentfilter)
        }

        if(permissionGranted()){
            recyclerView.visibility = View.VISIBLE
            permissionButton.visibility = View.GONE
            permissionTextView.visibility = View.GONE
            loadMusics()
        }else{
            recyclerView.visibility = View.GONE
            permissionButton.visibility = View.VISIBLE
            permissionTextView.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryCheckReceiver)
        unregisterReceiver(mainReceiver)
    }

    private fun permissionGranted():Boolean{
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Manifest.permission.READ_MEDIA_AUDIO
        }else{
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionLaunch(){
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            recyclerView.visibility = View.VISIBLE
            permissionButton.visibility = View.GONE
            permissionTextView.visibility = View.GONE
            loadMusics()
        } else {
            requestPermission.launch(permission)
        }
    }

    private fun permissionDialog(requestDialog : Boolean){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.permission_title_dialog)
        builder.setMessage(R.string.permission_message_dialog)
        builder.setPositiveButton(R.string.ok){dialog, _->
            dialog.dismiss()
            if(!requestDialog){
                val intent = Intent()
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            }
        }
        builder.setNegativeButton(R.string.no){dialog, _->
            dialog.dismiss()
        }
        builder.show()
    }


    private fun loadMusics(){
        val list = ArrayList<ListData>()

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID)

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use{ cursor->
            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val nameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val timeIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            while (cursor.moveToNext()){
                val id = cursor.getLong(idIndex)
                val title = cursor.getString(titleIndex)?:getString(R.string.default_title)
                val name = cursor.getString(nameIndex)?:getString(R.string.default_name)
                val time = cursor.getLong(timeIndex)
                val albumId = cursor.getLong(albumIdIndex)
                val musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                list.add(ListData(musicUri, albumId, title, name, time))
            }
        }

        recyclerView.adapter = RecyclerViewAdapter(list, object:RecyclerViewAdapter.OnItemClickListener{
            override fun itemClick(item:ListData){
                playMusic(item)
            }
        })
    }

    private fun playMusic(item:ListData) {
        serviceIntent.putExtra(MusicService.MUSIC_URI_KEY, item.musicUri.toString())
        serviceIntent.putExtra(MusicService.MUSIC_TITLE_KEY, item.title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private val batteryCheckReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BATTERY_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(BATTERY_SCALE, -1) ?: -1

            if (level != -1 && scale != -1) {
                val batteryPercent = (level / scale.toFloat()) * 100

                if (batteryPercent <= 20f) {
                    Toast.makeText(context, R.string.battery_alert_message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun serviceStart(view : View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        }
        else{
            startService(serviceIntent)
        }
    }

    fun serviceStop(view : View){
        stopService(serviceIntent)
    }
}




