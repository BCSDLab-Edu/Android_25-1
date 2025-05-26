package com.example.bcsd_android_2025_1.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.bcsd_android_2025_1.R
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.toDuration(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes =
        TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(hours)
    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes)

    val duration = if (hours.toInt() != 0) {
        String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
    }
    return duration
}

fun Uri.getAlbumArt(
    context: Context,
    resources: Resources,
    width: Int = 500,
    height: Int = 500
): Drawable? {
    var inputStream: InputStream? = null
    val albumArt: Drawable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        try {
            context.contentResolver.loadThumbnail(this, Size(500, 500), null)
                .toDrawable(resources)
        } catch (e: FileNotFoundException) {
            ResourcesCompat.getDrawable(resources, R.drawable.ic_no_album_art, null)
        }
    } else {
        try {
            inputStream = context.contentResolver.openInputStream(this)
            val option = BitmapFactory.Options()
            option.outWidth = width
            option.outHeight = height
            option.inSampleSize = 2
            BitmapFactory.decodeStream(inputStream, null, option)?.toDrawable(resources)
        } catch (e: FileNotFoundException) {
            ResourcesCompat.getDrawable(resources, R.drawable.ic_no_album_art, null)
        }
    }
    inputStream?.close()

    return albumArt
}
