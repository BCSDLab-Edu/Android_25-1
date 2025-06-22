package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AudioAdapter : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>(){


    private val items = mutableListOf<MainActivity.AudioItem>()

    fun submitList(newList: List<MainActivity.AudioItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_rv_item_title)
        val artistTextView: TextView = itemView.findViewById(R.id.tv_rv_item_artist)
        val timeTextView: TextView = itemView.findViewById(R.id.tv_rv_item_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.name
        holder.artistTextView.text = item.artist

        val minutes = item.duration/1000/60
        val seconds = (item.duration/1000)%60
        holder.timeTextView.text = String.format("%d:%02d", minutes, seconds)
    }

    override fun getItemCount(): Int = items.size



}