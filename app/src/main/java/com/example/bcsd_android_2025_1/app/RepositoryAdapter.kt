package com.example.bcsd_android_2025_1.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsd_android_2025_1.data.Repository
import com.example.bcsd_android_2025_1.databinding.ItemRepositoryBinding

class RepositoryAdapter (
    private val onClick: (Repository) -> Unit
) : PagingDataAdapter<Repository, RepositoryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repository) {
            binding.repositoryNameTextview.text = repo.name
            binding.root.setOnClickListener { onClick(repo) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(old: Repository, new: Repository) = old.id == new.id
            override fun areContentsTheSame(old: Repository, new: Repository) = old == new
        }
    }
}