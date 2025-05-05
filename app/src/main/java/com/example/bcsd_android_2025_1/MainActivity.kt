package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var countText: TextView

    // Activity Result API 사용 (더 이상 onActivityResult 비추천)
    private val randomActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val randomValue = data?.getIntExtra("RANDOM_VALUE", count) ?: count
            count = randomValue
            countText.text = count.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countText = findViewById(R.id.text_view)

        findViewById<Button>(R.id.button_toast).setOnClickListener {
            Toast.makeText(this, "Toast!!!!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_count).setOnClickListener {
            count++
            countText.text = count.toString()
        }

        findViewById<Button>(R.id.button_random).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("CURRENT_COUNT", count)
            randomActivityLauncher.launch(intent)
        }
    }
}