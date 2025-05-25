package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import androidx.activity.addCallback
import androidx.core.os.bundleOf

class SecondActivity : AppCompatActivity() {
    var randomNumber = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textviewSecondNumber: TextView = findViewById(R.id.textview_second_number)
        val textviewSecondInformation: TextView = findViewById(R.id.textview_second_information)

        val information = getString(R.string.text_information)

        if (intent.hasExtra(MainActivity.KEY_COUNT_NUMBER)) {
            val count = intent.getIntExtra(MainActivity.KEY_COUNT_NUMBER, 0)
            randomNumber = (0 until count+1).random()
            textviewSecondNumber.text = randomNumber.toString()
            textviewSecondInformation.text = information.plus(" $count")
        }

        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@SecondActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra(MainActivity.KEY_RANDOM_NUMBER, randomNumber)
            }
            startActivity(intent)
            finish()
        }
    }
}