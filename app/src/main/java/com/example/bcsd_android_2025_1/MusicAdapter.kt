package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(
    private val musicList: List<Music>,
    private val onItemClick: (Music) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_text)
        val artist: TextView = view.findViewById(R.id.artist_text)
        val duration: TextView = view.findViewById(R.id.duration_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.title.text = music.title
        holder.artist.text = music.artist
        holder.duration.text = formatDuration(music.duration)
        holder.itemView.setOnClickListener { onItemClick(music) }
    }

    override fun getItemCount(): Int = musicList.size

    private fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000 % 60
        val minutes = durationMs / (1000 * 60) % 60
        val hours = durationMs / (1000 * 60 * 60)
        return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds)
        else "%02d:%02d".format(minutes, seconds)
    }
}
