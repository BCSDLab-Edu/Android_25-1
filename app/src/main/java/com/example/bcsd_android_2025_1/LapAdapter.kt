package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapAdapter : RecyclerView.Adapter<LapAdapter.ViewHolder>() {

    private val lapTimes = mutableListOf<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapMessage: TextView = itemView.findViewById(R.id.tv_item_message)
        val lapRecord: TextView = itemView.findViewById(R.id.tv_item_record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lapMessage.text = holder.itemView.context.getString(R.string.tv_message)
        holder.lapRecord.text = lapTimes[position]
    }

    override fun getItemCount(): Int = lapTimes.size

    fun addLap(lap: String) {
        lapTimes.add(0, lap)
        notifyItemInserted(0)
    }

    fun clear() {
        lapTimes.clear()
        notifyDataSetChanged()
    }

}