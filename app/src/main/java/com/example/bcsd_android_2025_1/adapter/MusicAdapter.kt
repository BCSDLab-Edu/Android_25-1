package com.example.bcsd_android_2025_1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.R
import com.example.bcsd_android_2025_1.model.MusicData
import com.example.bcsd_android_2025_1.utils.getAlbumArt
import com.example.bcsd_android_2025_1.utils.toDurationFromMillisecond

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    var dataList = mutableListOf<MusicData>()
    lateinit var onClickListener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val item = dataList[position]
            titleTextView.text = item.title
            artistTextView.text = item.artist
            durationTextView.text = item.duration.toDurationFromMillisecond()

            val albumArt = item.albumUri.toUri().getAlbumArt(itemView.context, itemView.resources)
            albumArtImage.setImageDrawable(albumArt)

            itemView.setOnClickListener {
                onClickListener.onClick(item)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        val artistTextView: TextView = itemView.findViewById(R.id.artist_text_view)
        val durationTextView: TextView = itemView.findViewById(R.id.duration_text_view)
        val albumArtImage: ImageView = itemView.findViewById(R.id.album_art_image)
    }

    interface OnClickListener {
        fun onClick(music: MusicData)
    }

    inline fun setOnClickListener(crossinline item: (MusicData) -> Unit) {
        this.onClickListener = object : OnClickListener {
            override fun onClick(music: MusicData) {
                item(music)
            }
        }
    }
}
