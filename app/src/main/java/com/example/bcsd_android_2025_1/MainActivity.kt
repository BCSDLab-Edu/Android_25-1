package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WordViewModel by viewModels()
    private lateinit var editActivityLauncher: ActivityResultLauncher<Intent>


    private val adapter by lazy {
        WordAdapter(onTopClick = { viewModel.setTopWord(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val editedWord = data?.getStringExtra("edited_word") ?: ""
                    val editedMeaning = data?.getStringExtra("edited_meaning") ?: ""

                    // 상단 TextView 업데이트
                    binding.wordTextview.text = editedWord
                    binding.meaningTextview.text = editedMeaning
                }
            }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.editButton.setOnClickListener {
            viewModel.topWord.value?.let {
                val intent = Intent(this, AddEditActivity::class.java).apply {
                    putExtra("word_id", it.id)
                    putExtra("word_text", it.word)
                    putExtra("word_meaning", it.meaning)
                }
                editActivityLauncher.launch(intent)
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.topWord.value?.let {
                viewModel.delete(it)
                binding.wordTextview.text = ""
                binding.meaningTextview.text = ""
            }
        }

        viewModel.allWords.observe(this) {
            adapter.submitList(it)
        }

        viewModel.topWord.observe(this) {
            binding.wordTextview.text = it?.word ?: "단어 선택 필요"
            binding.meaningTextview.text = it?.meaning ?: ""
        }

        binding.floatingButton.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
    }
}