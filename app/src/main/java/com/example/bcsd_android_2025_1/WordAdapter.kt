package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.databinding.ItemWordBinding

class WordAdapter (
    private val onTopClick: (WordListData) ->Unit
) : ListAdapter<WordListData, WordAdapter.WordViewHolder>(DiffCallback()){

    inner class WordViewHolder(private val binding: ItemWordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordListData) {
            binding.word = word
            binding.root.setOnClickListener { onTopClick(word) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int):WordViewHolder{
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position:Int){
        holder.bind(getItem(position))
    }

    class DiffCallback: DiffUtil.ItemCallback<WordListData>(){
        override fun areItemsTheSame(oldItem:WordListData, newItem:WordListData):Boolean = oldItem.id ==newItem.id
        override fun areContentsTheSame(oldItem: WordListData, newItem: WordListData): Boolean = oldItem == newItem
    }
}