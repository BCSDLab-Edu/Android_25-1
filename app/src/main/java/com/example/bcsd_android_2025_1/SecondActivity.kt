package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("count_value", 0)

        val randomValue = (0 until count+1).random()

        val textView = findViewById<TextView>(R.id.Count)
        textView.text = "$randomValue"

        val resultIntent = Intent()
        resultIntent.putExtra("random_value", randomValue)

        setResult(RESULT_OK, resultIntent)
    }
}