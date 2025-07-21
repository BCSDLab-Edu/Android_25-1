package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bcsd_android_2025_1.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    companion object{
        const val wordIdKey = "word_id"
        const val wordTextKey = "word_text"
        const val wordMeaningKey = "word_meaning"
        const val wordImageKey = "word_imageUri"
        const val editedWordKey = "edited_word"
        const val editedMeaningKey = "edited_meaning"
        const val editedImageKey = "edited_image"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WordViewModel by viewModels()
    private lateinit var editActivityLauncher: ActivityResultLauncher<Intent>


    private val adapter by lazy {
        WordAdapter(onTopClick = { viewModel.setTopWord(it) })
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startAddEditActivity()
        } else {
            Toast.makeText(this, R.string.need_image_permission, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissionAndStart() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                startAddEditActivity()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                requestPermissionLauncher.launch(permission)
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun startAddEditActivity() {
        val intent = Intent(this, AddEditActivity::class.java)
        editActivityLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val editedWord = data?.getStringExtra(editedWordKey) ?: ""
                    val editedMeaning = data?.getStringExtra(editedMeaningKey) ?: ""
                    val editedImage = data?.getStringExtra(editedImageKey)

                    binding.wordTextview.text = editedWord
                    binding.meaningTextview.text = editedMeaning

                    if(!editedImage.isNullOrEmpty()){
                        Glide.with(this).load(editedImage).into(binding.wordImageview)
                    }else{
                        binding.wordImageview.setImageDrawable(null)
                    }
                }
            }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.editButton.setOnClickListener {
            viewModel.topWord.value?.let {
                val intent = Intent(this, AddEditActivity::class.java).apply {
                    putExtra(wordIdKey, it.id)
                    putExtra(wordTextKey, it.word)
                    putExtra(wordMeaningKey, it.meaning)
                    putExtra(wordImageKey, it.imageUri)
                }
                editActivityLauncher.launch(intent)
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.topWord.value?.let {
                viewModel.delete(it)
                binding.wordTextview.text = ""
                binding.meaningTextview.text = ""
                binding.wordImageview.setImageDrawable(null)
            }
        }

        viewModel.allWords.observe(this) {
            adapter.submitList(it)
        }

        viewModel.topWord.observe(this) {
            binding.wordTextview.text = it?.word ?: ""
            binding.meaningTextview.text = it?.meaning ?: ""
            if (!it?.imageUri.isNullOrEmpty()) {
                Glide.with(this).load(it?.imageUri).into(binding.wordImageview)
            } else {
                binding.wordImageview.setImageDrawable(null)
            }
        }

        binding.floatingButton.setOnClickListener {
            checkPermissionAndStart()
        }
    }
}