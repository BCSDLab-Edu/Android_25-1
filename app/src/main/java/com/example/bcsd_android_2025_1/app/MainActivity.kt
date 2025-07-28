package com.example.bcsd_android_2025_1.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val adapter = RepositoryAdapter {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.repositoryRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.repositoryRecyclerview.adapter = adapter

        lifecycleScope.launch {
            viewModel.repositories.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
            }
        }

        binding.searchEdittext.doOnTextChanged { text, _, _, _ ->
            viewModel.onQueryChanged(text.toString())
        }

        lifecycleScope.launch {
            viewModel.repositories.collectLatest { pagingData ->
                binding.progressBar.isVisible = true
                adapter.submitData(pagingData = pagingData)
                binding.progressBar.isVisible = false
            }
        }

        adapter.addLoadStateListener {
            binding.progressBar.isVisible = it.refresh is LoadState.Loading
        }
    }
}