package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat

class SecondActivity : AppCompatActivity() {

    private var randomNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(this, R.color.darkblue))
        )

        val count = intent.getIntExtra("count", 0)

        randomNumber = Random.nextInt(0, count + 1)

        val infoText = findViewById<TextView>(R.id.textview_random_info)
        val numberText = findViewById<TextView>(R.id.textview_random_number)

        infoText.text = "Here is a random number between 0 and $count"
        numberText.text = randomNumber.toString()
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("randomNumber", randomNumber)
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()
    }
}