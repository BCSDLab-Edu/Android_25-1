package com.example.bcsd_android_2025_1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private val context: Context,
    private val names: MutableList<String>
) : RecyclerView.Adapter<RecyclerViewAdapter.NameViewHolder>() {

    inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(android.R.id.text1)

        init {
            itemView.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("remove confirm")
                    .setMessage("\"${names[adapterPosition]}\" remove??")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("remove") { _, _ ->
                        names.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                    .setNegativeButton("cancel", null)
                    .show()
            }
            itemView.setOnLongClickListener {
                val editText = EditText(context).apply {
                    setText(names[adapterPosition])
                }

                AlertDialog.Builder(context)
                    .setTitle("change name")
                    .setView(editText)
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("confirm") { _, _ ->
                        val newName = editText.text.toString().trim()
                        if (newName.isNotEmpty()) {
                            names[adapterPosition] = newName
                            notifyItemChanged(adapterPosition)
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()

                true
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return NameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.nameText.text = names[position]
        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, R.color.citrus)
        )
        holder.nameText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))

    }

    override fun getItemCount(): Int = names.size

    fun add_name(name: String) {
        names.add(name)
        notifyItemInserted(names.size - 1)
    }
}
