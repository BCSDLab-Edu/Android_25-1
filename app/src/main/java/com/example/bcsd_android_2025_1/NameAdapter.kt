package com.example.bcsd_android_2025_1
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class NameAdapter(
    private val names: MutableList<String>,
    private val onClick: (Int) -> Unit,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<NameAdapter.ViewHolder>() {

    inner class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnClickListener {
                onClick(adapterPosition)
            }
            textView.setOnLongClickListener {
                onLongClick(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply{ val marginInPx = (5 * resources.displayMetrics.density).toInt()
                setMargins(marginInPx, marginInPx, marginInPx, marginInPx)
            }
            setPadding(20, 20, 20, 20)
            textSize = 18f
            setBackgroundColor("#FFA500".toColorInt())
            setTextColor(Color.WHITE)
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = names[position]
    }

    override fun getItemCount(): Int = names.size
}
