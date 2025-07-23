package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bcsd_android_2025_1.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private val viewModel: WordViewModel by viewModels()

    private var imageUri: String? = null
    private var wordId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editWordText = intent.getStringExtra(MainActivity.wordTextKey)
        val editMeaning = intent.getStringExtra(MainActivity.wordMeaningKey)
        wordId = intent.getIntExtra(MainActivity.wordIdKey, -1).takeIf { it != -1 }

        val image = intent.getStringExtra(MainActivity.wordImageKey)
        imageUri = image

        if (!imageUri.isNullOrEmpty()) {
            Glide.with(this).load(imageUri).into(binding.addImageImageview)
        } else {
            binding.addImageImageview.setImageDrawable(null)
        }

        binding.wordEdittext.setText(editWordText)
        binding.meaningEdittext.setText(editMeaning)

        binding.addImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.addButton.setOnClickListener {
            val wordText = binding.wordEdittext.text.toString()
            val meaning = binding.meaningEdittext.text.toString()
            if (wordText.isNotBlank() && meaning.isNotBlank()) {
                val word = WordListData(wordId ?: 0, wordText, meaning, imageUri)
                if (wordId != null) {
                    viewModel.update(word)
                    binding.wordEdittext.setText("")
                    binding.meaningEdittext.setText("")
                    binding.addImageImageview.setImageDrawable(null)
                    imageUri = null
                    val resultIntent = Intent().apply {
                        putExtra(MainActivity.editedWordKey, word.word)
                        putExtra(MainActivity.editedMeaningKey, word.meaning)
                        putExtra(MainActivity.editedImageKey, word.imageUri)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                } else {
                    viewModel.insert(word)
                }
                finish()
            }
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri.toString()
                Glide.with(this).load(uri).into(binding.addImageImageview)
            } else {
                imageUri = null
                binding.addImageImageview.setImageDrawable(null)
            }
        }
}