package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val itemList = mutableListOf<ListData>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.text_item_title)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder(itemView.context)
                        .setTitle(R.string.recyclerView_title)
                        .setMessage(R.string.recyclerView_message)
                        .setPositiveButton(R.string.recyclerView_delete) { dialog, _ ->
                            itemList.removeAt(position)
                            notifyItemRemoved(position)
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.recyclerView_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val editText = EditText(itemView.context)
                    editText.setText(itemList[position].title)

                    AlertDialog.Builder(itemView.context)
                        .setView(editText)
                        .setPositiveButton(R.string.recyclerView_okay) { dialog, _ ->
                            val newTitle = editText.text.toString().trim()
                            if (newTitle.isNotEmpty()) {
                                itemList[position] = ListData(newTitle)
                                notifyItemChanged(position)
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.recyclerView_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = itemList[position].title
    }

    override fun getItemCount(): Int = itemList.size

    fun addItem(item: ListData) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }
}
