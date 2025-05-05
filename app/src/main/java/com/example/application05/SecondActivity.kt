package com.example.application05

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.os.Handler
import android.os.Looper

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("count", 0)
        val randomNumber = Random.nextInt(0, count + 1)

        val textView: TextView = findViewById(R.id.random_text_view)
        textView.text = randomNumber.toString()

        Handler(Looper.getMainLooper()).postDelayed({
            val resultIntent = Intent()
            resultIntent.putExtra("selectedNumber", randomNumber)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }, 1000)
    }
}