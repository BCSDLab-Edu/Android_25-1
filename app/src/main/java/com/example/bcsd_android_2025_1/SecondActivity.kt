package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        val max = intent.getIntExtra(MainActivity.EXTRA_COUNT, 0)

        val random = if (max > 0) Random.nextInt(0, max + 1) else 0

        val result = findViewById<TextView>(R.id.view_random)
        result.text = getString(R.string.result, random)

        getSharedPreferences("prefer", MODE_PRIVATE)
            .edit()
            .putInt("new_random", random)
            .apply()

        findViewById<Button>(R.id.button_select).setOnClickListener {
            finish()
        }
    }
}
