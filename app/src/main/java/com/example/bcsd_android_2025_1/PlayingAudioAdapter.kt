package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayingAudioAdapter : RecyclerView.Adapter<PlayingAudioAdapter.PlayingAudioHolder>(){


    private var playItem : MainActivity.AudioItem? = null

    fun setItem(item: MainActivity.AudioItem){
        playItem = item
        notifyDataSetChanged()
    }

    inner class PlayingAudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playTitle = itemView.findViewById<TextView>(R.id.tv_item_title)
        val playArtist = itemView.findViewById<TextView>(R.id.tv_item_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayingAudioHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return PlayingAudioHolder(view)
    }

    override fun getItemCount(): Int = if (playItem != null) 1 else 0

    override fun onBindViewHolder(holder: PlayingAudioHolder, position: Int) {
        playItem?.let {
            holder.playTitle.text = it.name
            holder.playArtist.text = it.artist
        }
    }
}