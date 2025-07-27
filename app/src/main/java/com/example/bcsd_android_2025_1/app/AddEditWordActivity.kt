package com.example.bcsd_android_2025_1.app
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bcsd_android_2025_1.R
import com.example.bcsd_android_2025_1.domain.Word
import com.example.bcsd_android_2025_1.databinding.ActivityAddEditWordBinding
import com.bumptech.glide.Glide

class AddEditWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditWordBinding

    private var selectedImageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            Glide.with(this)
                .load(uri)
                .into(binding.ivPreview)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val existingWord = intent.getSerializableExtra("word") as? Word

        if (existingWord != null) {
            binding.etWord.setText(existingWord.word)
            binding.etMeaning.setText(existingWord.meaning)

            if (!existingWord.imageUri.isNullOrEmpty()) {
                selectedImageUri = Uri.parse(existingWord.imageUri)
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.ivPreview)
            }
        }

        binding.btnImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            val word = binding.etWord.text.toString()
            val meaning = binding.etMeaning.text.toString()
            if (word.isNotBlank() && meaning.isNotBlank()) {
                val result = Word(
                    id = existingWord?.id ?: 0,
                    word = word,
                    meaning = meaning,
                    imageUri = selectedImageUri?.toString()
                )
                setResult(RESULT_OK, Intent().putExtra("result", result))
                finish()
            } else {
                Toast.makeText(this, R.string.toast_msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}