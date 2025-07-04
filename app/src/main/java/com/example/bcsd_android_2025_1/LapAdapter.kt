package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapAdapter : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    private val lapList = mutableListOf<LapItem>()

    inner class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lapTextView: TextView = itemView.findViewById(R.id.text_lap)

        fun bind(item: LapItem) {
            lapTextView.text = item.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lap, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.bind(lapList[position])
    }

    override fun getItemCount(): Int = lapList.size

    fun addLap(time: String) {
        lapList.add(0, LapItem(time))
        notifyItemInserted(0)
    }

    fun clear() {
        lapList.clear()
        notifyDataSetChanged()
    }
}
