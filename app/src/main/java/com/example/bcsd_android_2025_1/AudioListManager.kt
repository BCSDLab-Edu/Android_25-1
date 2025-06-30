package com.example.bcsd_android_2025_1

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AudioListManager(application: Application) : AndroidViewModel(application) {
    private val audioListIn = MutableLiveData<List<MainActivity.AudioItem>>()
    val audioListObserve: LiveData<List<MainActivity.AudioItem>> get() = audioListIn

    fun loadAudioFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<MainActivity.AudioItem>()
            val resolver = getApplication<Application>().contentResolver

            val projection = arrayOf(
                Media._ID,
                Media.DISPLAY_NAME,
                Media.ARTIST,
                Media.DURATION
            )

            val selection= "${Media.IS_MUSIC} != 0 AND ${Media.DURATION} > 0"
            val selectionArgs: Array<String>? = null

            val sortOrder = "${Media.DATE_ADDED} DESC"

            resolver.query(
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
                    val uri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id)

                    list.add(MainActivity.AudioItem(id, name, artists, duration, uri))

                }
            }

            audioListIn.postValue(list)
        }
    }
}