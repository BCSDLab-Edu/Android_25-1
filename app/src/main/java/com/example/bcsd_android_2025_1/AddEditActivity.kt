package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bcsd_android_2025_1.databinding.ActivityAddEditBinding


class AddEditActivity : AppCompatActivity(){
    private lateinit var binding: ActivityAddEditBinding
    private val viewModel:WordViewModel by viewModels()

    private var wordId: Int?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editWordText = intent.getStringExtra("word_text")
        val editMeaning = intent.getStringExtra("word_meaning")
        wordId = intent.getIntExtra("word_id", -1).takeIf { it != -1 }

        binding.wordEdittext.setText(editWordText)
        binding.meaningEdittext.setText(editMeaning)

        binding.addButton.setOnClickListener {
            val wordText = binding.wordEdittext.text.toString()
            val meaning = binding.meaningEdittext.text.toString()
            if (wordText.isNotBlank() && meaning.isNotBlank()) {
                val word = WordListData(wordId ?: 0, wordText, meaning)
                if (wordId != null) {
                    viewModel.update(word)
                    binding.wordEdittext.setText("")
                    binding.meaningEdittext.setText("")
                    val resultIntent = Intent()
                    resultIntent.putExtra("edited_word", word.word)
                    resultIntent.putExtra("edited_meaning", word.meaning)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                } else {
                    viewModel.insert(word)
                }
                finish()
            }
        }
    }
}