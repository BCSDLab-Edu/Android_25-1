package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(private val musicList: List<Music>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.text_list_Title)
        val artistText: TextView = view.findViewById(R.id.text_list_Artist)
        val durationText: TextView = view.findViewById(R.id.text_list_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.titleText.text = music.title
        holder.artistText.text = music.artist

        val seconds = (music.duration / 1000) % 60
        val minutes = (music.duration / (1000 * 60)) % 60
        holder.durationText.text = String.format("%02d:%02d", minutes, seconds)

        holder.view.setOnClickListener {
            Toast.makeText(holder.view.context,  "재생: ${music.title}", Toast.LENGTH_SHORT).show()
        }
    }

        override fun getItemCount(): Int = musicList.size
}


