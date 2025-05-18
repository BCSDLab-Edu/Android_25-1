package com.example.bcsd_android_2025_1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {

    private var Count = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val textViewRandom = findViewById<TextView>(R.id.view_random)
        val Select = findViewById<Button>(R.id.button_select)

        Count = intent.getIntExtra(MainActivity.EXTRA_COUNT, 0)

        val randomValue = Random.nextInt(0, Count + 1)
        textViewRandom.text = "Random: $randomValue"

        Select.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.EXTRA_RANDOM, randomValue)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
