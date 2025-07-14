package com.example.bcsd_android_2025_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.databinding.ItemWordBinding

class WordAdapter(
    private val onItemClick: (Word) -> Unit
) : ListAdapter<Word, WordAdapter.WordViewHolder>(DiffCallback()){
    inner class WordViewHolder(private val binding: ItemWordBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.itemTvWord.text = word.word
            binding.itemTvMean.text = word.meaning
            binding.root.setOnClickListener { onItemClick(word) }
        }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem
        }
}