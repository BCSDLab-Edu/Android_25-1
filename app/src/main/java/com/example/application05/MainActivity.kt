package com.example.application05

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application05.databinding.ActivityMainBinding
import android.widget.Toast
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCountButton.setOnClickListener {
            count += 1
            binding.screenTextViewCount.text = count.toString()
        }

        binding.buttonRandomButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("count", count)
            randomResultLauncher.launch(intent)
        }

        binding.buttonToastButton.setOnClickListener {
            Toast.makeText(this,"Android",Toast.LENGTH_SHORT).show()
        }
    }

    private val randomResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedNumber = data?.getIntExtra("selectedNumber", 0)
            selectedNumber?.let {
                count = it
                binding.screenTextViewCount.text = it.toString()
            }
        }
    }
}