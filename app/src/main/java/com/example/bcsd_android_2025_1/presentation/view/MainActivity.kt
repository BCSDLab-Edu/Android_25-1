package com.example.bcsd_android_2025_1.presentation.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsd_android_2025_1.WordAdapter
import com.example.bcsd_android_2025_1.data.model.Word
import com.example.bcsd_android_2025_1.data.repository.WordRepository
import com.example.bcsd_android_2025_1.data.room.AppDatabase
import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding
import com.example.bcsd_android_2025_1.domain.usecase.DeleteWord
import com.example.bcsd_android_2025_1.domain.usecase.InsertWord
import com.example.bcsd_android_2025_1.domain.usecase.UpdateWord
import com.example.bcsd_android_2025_1.presentation.viewmodel.WordViewModel
import com.example.bcsd_android_2025_1.presentation.viewmodel.WordViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordViewModel: WordViewModel
    private lateinit var wordAdapter: WordAdapter

    private var selectedWord: Word? = null

    private val editWordLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updateWord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra("updated_word", Word::class.java)
            } else {
                result.data?.getSerializableExtra("updated_word") as? Word
            }
            updateWord?.let {
                wordViewModel.update(it)
                selectedWord = it
                showSelectedWord(it)
            }

            val newWord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra("new_word", Word::class.java)
            } else {
                result.data?.getSerializableExtra("new_word") as? Word
            }

            newWord?.let {
                wordViewModel.insert(it)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = AppDatabase.getDatabase(applicationContext).wordDao()
        val repository = WordRepository(dao)

        val insert = InsertWord(repository)
        val update = UpdateWord(repository)
        val delete = DeleteWord(repository)

        val allWords = repository.getAllWords()
        val factory = WordViewModelFactory(insert, update, delete, allWords)
        wordViewModel = ViewModelProvider(this, factory)[WordViewModel::class.java]

        wordAdapter = WordAdapter { word ->
            selectedWord = word
            showSelectedWord(word)
        }

        binding.recyclerviewMain.adapter = wordAdapter
        binding.recyclerviewMain.layoutManager = LinearLayoutManager(this)

        wordViewModel.allWords.observe(this) { words ->
            wordAdapter.submitList(words)
        }

        binding.imageBtnMainEdit.setOnClickListener {
            selectedWord?.let {
                val intent = Intent(this, AddEditActivity::class.java)
                intent.putExtra("word_to_edit", it)
                editWordLauncher.launch(intent)
            }
        }

        binding.imageBtnMainPlus.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            editWordLauncher.launch(intent)
        }

        binding.imageBtnMainDelete.setOnClickListener {
            selectedWord?.let {
                wordViewModel.delete(it)
                selectedWord = null
                binding.tvMainWord.text = ""
                binding.tvMainMean.text = ""
            }
        }

    }

    private fun showSelectedWord(word: Word) {
        binding.tvMainWord.text = word.word
        binding.tvMainMean.text = word.meaning
    }
}