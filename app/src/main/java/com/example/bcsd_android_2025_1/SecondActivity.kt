package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    private var random = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("COUNT", 0)
        random = (0..count).random()

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "RANDOM: $random"

        findViewById<Button>(R.id.button_random).setOnClickListener {
            returnResult()
        }
    }

    private fun returnResult() {
        val intent = Intent()
        intent.putExtra("COUNT", random)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        returnResult()
    }
}
