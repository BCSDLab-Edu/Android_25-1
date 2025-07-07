package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.main_button_start)

        startButton.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) {
                startButton.text = "Pause"
            } else {
                startButton.text = "Start"
            }
        }
    }
}