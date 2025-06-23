package com.example.bcsd_android_2025_1

import android.Manifest
import android.content.ContentUris
import android.content.Intent
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

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var permissionTextView: TextView
    private lateinit var permissionButton: Button

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

        recyclerView = findViewById(R.id.recycler_view)
        permissionTextView = findViewById(R.id.need_permission_textview)
        permissionButton = findViewById(R.id.need_permission_button)

        if(permissionGranted()){
            recyclerView.visibility = View.VISIBLE
            permissionButton.visibility = View.GONE
            permissionTextView.visibility = View.GONE
            loadMusics()
        }else{
            recyclerView.visibility = View.GONE
            permissionButton.visibility = View.VISIBLE
            permissionTextView.visibility = View.VISIBLE
            requestPermissionLaunch()
        }

        permissionButton.setOnClickListener{
            val intent = Intent()
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun permissionGranted():Boolean{
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Manifest.permission.READ_MEDIA_AUDIO
        }else{
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionLaunch(){
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Manifest.permission.READ_MEDIA_AUDIO
        }else{
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        requestPermission.launch(permission)
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
                val title = cursor.getString(titleIndex)?:R.string.default_name.toString()
                val name = cursor.getString(nameIndex)?:R.string.default_name.toString()
                val time = cursor.getLong(timeIndex)
                val albumId = cursor.getLong(albumIdIndex)
                val musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                list.add(ListData(musicUri, albumId, title, name, time))
            }
        }
        recyclerView.adapter = RecyclerViewAdapter(list)
    }
}

