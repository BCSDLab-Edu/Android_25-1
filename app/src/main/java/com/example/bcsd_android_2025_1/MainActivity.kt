package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_login_main)
        setSupportActionBar(toolbar)

        val toastButton = findViewById<Button>(R.id.button_login_toast)
        toastButton.setOnClickListener {
            Toast.makeText(this, "와우!!", Toast.LENGTH_SHORT).show()
        }
    }

}