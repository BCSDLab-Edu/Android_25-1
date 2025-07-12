package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter:RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
    private val items = mutableListOf<ListData>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val lapItemTextView: TextView = view.findViewById(R.id.lap_item_textview)

        fun bind(item: ListData) {
            lapItemTextView.text = item.time
        }
    }

    fun addLapItem(time:String){
        items.add(0, ListData(time))
        notifyItemInserted(0)
    }

    fun removeAllItems(){
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int{
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}