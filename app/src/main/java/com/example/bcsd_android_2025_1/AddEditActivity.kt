package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bcsd_android_2025_1.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private var originalWord: Word? = null
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordViewModel = ViewModelProvider(this)[WordViewModel::class.java]

        originalWord = intent.getSerializableExtra("word_to_edit") as? Word
        originalWord?.let {
            binding.editTvAddWord.setText(it.word)
            binding.editTvAddMean.setText(it.meaning)
        }

        binding.button.setOnClickListener {
            val word = binding.editTvAddWord.text.toString()
            val mean = binding.editTvAddMean.text.toString()

            if (word.isBlank() || mean.isBlank()) {
                return@setOnClickListener
            }

            if (originalWord != null) {
                val updated = originalWord!!.copy(word = word, meaning = mean)
                val result = Intent().apply {
                    putExtra("updated_word", updated)
                }
                setResult(RESULT_OK, result)
                finish()
            } else {
                wordViewModel.insert(Word(word = word, meaning = mean))
                finish()
            }

        }
    }
}