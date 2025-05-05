package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentCount = intent.getIntExtra("CURRENT_COUNT", 0)
        val randomValue = Random.nextInt(0, currentCount + 1)

        val resultIntent = Intent().apply {
            putExtra("RANDOM_VALUE", randomValue)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()  // MainActivity로 돌아감
    }
}