package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import androidx.core.os.bundleOf

class SecondActivity : AppCompatActivity() {
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
        var randomNumber = 0


        if (intent.hasExtra("countNumber")) {
            val count = intent.extras?.getInt("countNumber")!!
            randomNumber = (0 until count+1).random()
            textviewSecondNumber.text = randomNumber.toString()
            textviewSecondInformation.text = information.plus(" $count")
        }


        val bundleSecond = bundleOf("keyRandomNumber" to randomNumber)
        val intentSecond = Intent(this, MainActivity::class.java).apply{
            putExtras(bundleSecond)
        }
        setResult(RESULT_OK, intentSecond)
    }


}