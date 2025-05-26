package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {
    var randomNumber = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val currentCount = intent.getIntExtra("currentCount", 0)
        val randomNumber = Random.nextInt(0, currentCount + 1)

        val randomTextView: TextView = findViewById(R.id.textview_random_message)
        randomTextView.text = "$randomNumber"

        saveRandomToPreferences(randomNumber)

    }

    private fun saveRandomToPreferences(random: Int) {
        val prefs = getSharedPreferences("rand_prefs", MODE_PRIVATE)
        prefs.edit().putInt("randomNumber", random).apply()
    }

}