package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LapAdapter : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    private val lapList = mutableListOf<LapItem>()

    class LapViewHolder(View: View) : RecyclerView.ViewHolder(View) {
        val lapText: TextView = View.findViewById(R.id.text_lap)
    }

    fun addLap(time: String) {
        lapList.add(0, LapItem(time))
        notifyItemInserted(0)
    }

    fun clearLaps() {
        lapList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lap, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.lapText.text = lapList[position].time
    }

    override fun getItemCount(): Int {
        return lapList.size
    }
}