package com.example.bcsd_android_2025_1

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val items:MutableList<ListData>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(inflatedView)
    }

    interface OnItemClickListener{
        fun itemClick(item:ListData)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListData, position: Int) {
            val imageItem: ImageView = view.findViewById(R.id.imageview_item)
            val titleItem: TextView = view.findViewById(R.id.title_textview_item)
            val nameItem: TextView = view.findViewById(R.id.name_textview_item)
            val timeItem: TextView = view.findViewById(R.id.time_textview_item)
            titleItem.text = item.title
            nameItem.text = item.name
            timeItem.text = item.time.timeSet()

            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(view.context, item.musicUri)

                val art = retriever.embeddedPicture
                if (art != null) {
                    val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
                    imageItem.setImageBitmap(bitmap)
                } else {
                    imageItem.setImageResource(R.drawable.album_image_default_aespa)
                }
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
                imageItem.setImageResource(R.drawable.album_image_default_aespa)
            }

            view.setOnClickListener{
                itemClickListener.itemClick(item)
            }
        }
    }

    private fun Long.timeSet():String{
        val hour = this/(1000*60*60)
        val min = (this/(1000*60))%60
        val sec = (this/1000)%60
        return if(hour>0){
            "%d:%02d:%02d".format(hour, min, sec)
        }else{
            "%02d:%02d".format(min,sec)
        }
    }
}
