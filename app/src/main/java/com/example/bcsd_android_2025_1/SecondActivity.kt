package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

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

        val textview_second_number: TextView = findViewById(R.id.textview_second_number)
        val textview_second_information: TextView = findViewById(R.id.textview_second_information)

        var information = "Here is a random number between 0 and "

        if (intent.hasExtra("count_number")) {
            textview_second_number.text = intent.getStringExtra("count_number")
            textview_second_information.text = information.plus(intent.getStringExtra("count_number"))
        }
    }
}