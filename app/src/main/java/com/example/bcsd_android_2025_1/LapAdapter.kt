package com.example.bcsd_android_2025_1

import LapData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapAdapter(private val lapList: List<LapData>) :
    RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    class LapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.lap_text_number)
        val section: TextView = view.findViewById(R.id.lap_text_sectionRecord)
        val total: TextView = view.findViewById(R.id.lap_text_totalTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lap_list, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val lap = lapList[position]
        holder.number.text = lap.number.toString()
        holder.section.text = lap.sectionTime
        holder.total.text = lap.totalTime
    }

    override fun getItemCount() = lapList.size
}
