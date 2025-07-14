package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding

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
                intent.getSerializableExtra("updated_word") as? Word
            }
            updateWord?.let {
                wordViewModel.update(it)
                selectedWord = it
                showSelectedWord(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordViewModel = ViewModelProvider(this)[WordViewModel::class.java]

        wordAdapter = WordAdapter { word ->
            selectedWord = word
            showSelectedWord(word)
        }

        binding.recyclerviewMain.adapter = wordAdapter
        binding.recyclerviewMain.layoutManager = LinearLayoutManager(this)

        wordViewModel.allWords.observe(this) { words ->
            wordAdapter.submitList(words)
        }

        binding.imageBtnMainDelete.setOnClickListener{
            selectedWord?.let{
                wordViewModel.delete(it)
                clearSelectedWord()
            }
        }
        binding.imageBtnMainEdit.setOnClickListener {
            selectedWord?.let{
                val intent = Intent(this, AddEditActivity::class.java)
                intent.putExtra("word_to_edit",it)
                editWordLauncher.launch(intent)
            }
        }
        binding.imageBtnMainPlus.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
    }
    private fun showSelectedWord(word: Word) {
        binding.tvMainWord.text = word.word
        binding.tvMainMean.text = word.meaning
    }
    private fun clearSelectedWord() {
        binding.tvMainWord.text = getString(R.string.tv_main_note_word)
        binding.tvMainMean.text = getString(R.string.tv_main_note_mean)
        selectedWord = null
    }
}