package com.example.bcsd_android_2025_1
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bcsd_android_2025_1.databinding.ActivityAddEditWordBinding

class AddEditWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditWordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val existingWord = intent.getSerializableExtra("word") as? Word

        if (existingWord != null) {
            binding.etWord.setText(existingWord.word)
            binding.etMeaning.setText(existingWord.meaning)
        }

        binding.btnSave.setOnClickListener {
            val word = binding.etWord.text.toString()
            val meaning = binding.etMeaning.text.toString()
            if (word.isNotBlank() && meaning.isNotBlank()) {
                val result = Word(
                    id = existingWord?.id ?: 0,
                    word = word,
                    meaning = meaning
                )
                setResult(RESULT_OK, Intent().putExtra("result", result))
                finish()
            } else {
                Toast.makeText(this, R.string.toast_msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}