package com.example.bcsd_android_2025_1
import android.Manifest
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(private val musicList: List<Triple<String, String, Long>>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tv_title)
        val artist = view.findViewById<TextView>(R.id.tv_artist)
        val time = view.findViewById<TextView>(R.id.tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val (title, artist, time) = musicList[position]
        holder.title.text = title
        holder.artist.text = artist
        holder.time.text = settime(time)
    }

    override fun getItemCount() = musicList.size

    private fun settime(timeMs: Long): String {
        val min = timeMs / 1000 / 60
        val sec = (timeMs / 1000 % 60)
        return String.format("%02d:%02d", min, sec)
    }
}