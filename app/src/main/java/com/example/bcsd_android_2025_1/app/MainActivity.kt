package com.example.bcsd_android_2025_1.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsd_android_2025_1.domain.Word
import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WordViewModel
    private lateinit var adapter: WordAdapter

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val word = result.data?.getSerializableExtra("result") as? Word
                word?.let {
                    if (it.id == 0) viewModel.insert(it) else viewModel.update(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WordViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = WordAdapter(
            onItemClick = { word -> viewModel.selectWord(word) },
            onEditClick = { word ->
                val intent = Intent(this, AddEditWordActivity::class.java)
                intent.putExtra("word", word)
                launcher.launch(intent)
            },
            onDeleteClick = { word -> viewModel.delete(word) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allWords.observe(this) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddEditWordActivity::class.java)
            launcher.launch(intent)
        }
    }
}