package com.example.bcsd_android_2025_1
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.MainActivity.ListItem

class NameAdapter(private val items: MutableList<ListItem>) :
    RecyclerView.Adapter<NameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.text_name)

        fun bind(item: ListItem) {
            nameText.text = item.name

            itemView.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Delet")
                    .setMessage("sure?")
                    .setPositiveButton("yes") { dialog, _ ->
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            items.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("no", null)
                    .show()
            }

            itemView.setOnLongClickListener {
                val editText = EditText(itemView.context)
                editText.setText(item.name)

                AlertDialog.Builder(itemView.context)
                    .setTitle("Modify")
                    .setView(editText)
                    .setPositiveButton("updated") { dialog, _ ->
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val newName = editText.text.toString()
                            if (newName.isNotBlank()) {
                                items[position] = ListItem(newName)
                                notifyItemChanged(position)
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("back", null)
                    .show()

                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
