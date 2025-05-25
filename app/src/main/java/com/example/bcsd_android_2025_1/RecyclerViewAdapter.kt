package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val items:ArrayList<ListData>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item, position)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListData, position: Int) {
            val textViewItem: TextView = view.findViewById(R.id.textview_item)
            textViewItem.text = item.title
            view.setOnClickListener {
                AlertDialog.Builder(view.context)
                    .setTitle(view.context.getString(R.string.text_dialog_caution))
                    .setMessage(view.context.getString(R.string.text_dialog_delete_message))
                    .setPositiveButton(view.context.getString(R.string.text_delete)) { dialog, _ ->
                        items.removeAt(position)
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton(view.context.getString(R.string.text_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            view.setOnLongClickListener {
                val editTextChange = EditText(view.context)
                editTextChange.hint = view.context.getString(R.string.edittext_change_hint)

                AlertDialog.Builder(view.context)
                    .setTitle(view.context.getString(R.string.text_dialog_caution))
                    .setMessage(view.context.getString(R.string.text_dialog_change_message))
                    .setView(editTextChange)
                    .setPositiveButton(view.context.getString(R.string.text_ok)) { dialog, _ ->
                        item.title=editTextChange.text.toString()
                        notifyItemChanged(adapterPosition)
                    }
                    .setNegativeButton(view.context.getString(R.string.text_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
        }
    }
}