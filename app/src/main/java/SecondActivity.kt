package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val currentCount = intent.getIntExtra("currentCount", 0)
        val randomNumber = Random.nextInt(0, currentCount + 1)

        val randomTextView: TextView = findViewById(R.id.textview_random)
        randomTextView.text ="$randomNumber"


        val resultIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("randomNumber", randomNumber)
        }

        setResult(Activity.RESULT_OK, resultIntent)
    }
}