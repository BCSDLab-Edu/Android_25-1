package com.example.bcsd_android_2025_1.presentation.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bcsd_android_2025_1.data.model.Word
import com.example.bcsd_android_2025_1.databinding.ActivityAddEditBinding


class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private var selectedImageUri: String? = null
    private var editingWord: Word? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            selectedImageUri = it.toString()
            binding.imagePreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editingWord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("word_to_edit", Word::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("word_to_edit") as? Word
        }

        editingWord?.let { word ->
            binding.editTvAddWord.setText(word.word)
            binding.editTvAddMean.setText(word.meaning)
            selectedImageUri = word.imageUri

            if (!word.imageUri.isNullOrEmpty()) {
                Glide.with(this)
                    .load(Uri.parse(word.imageUri))
                    .into(binding.imagePreview)
            }

        }

        binding.btnSelectImage.setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.btnAddAdd.setOnClickListener {
            val newWord = Word(
                id = editingWord?.id ?: 0,
                word = binding.editTvAddWord.text.toString(),
                meaning = binding.editTvAddMean.text.toString(),
                imageUri = selectedImageUri
            )

            val resultIntent = intent
            if (editingWord != null) {
                resultIntent.putExtra("updated_word", newWord)
            } else {
                resultIntent.putExtra("new_word", newWord)
            }

            setResult(RESULT_OK,resultIntent)
            finish()
        }

    }

}