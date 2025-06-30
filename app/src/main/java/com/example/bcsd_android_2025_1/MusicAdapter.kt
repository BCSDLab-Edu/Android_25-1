package com.example.bcsd_android_2025_1
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(
    private val musicList: List<MainActivity.MusicInfo>,
    private val itemClickListener: (MainActivity.MusicInfo) -> Unit) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tv_title)
        val artist = view.findViewById<TextView>(R.id.tv_artist)
        val time = view.findViewById<TextView>(R.id.tv_time)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    itemClickListener(musicList[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.title.text = music.title
        holder.artist.text = music.artist
        holder.time.text = music.time.toTimeString()
    }

    override fun getItemCount() = musicList.size

    private fun Long.toTimeString(): String {
        val min = this / 1000 / 60
        val sec = (this / 1000 % 60)
        return String.format("%02d:%02d", min, sec)
    }
}