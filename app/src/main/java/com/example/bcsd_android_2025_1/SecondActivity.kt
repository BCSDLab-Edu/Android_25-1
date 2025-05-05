package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import kotlin.random.Random
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val toolbar = findViewById<Toolbar>(R.id.toolbar_login_second)
        setSupportActionBar(toolbar)

        val number = intent.getIntExtra("number", 0)
        val random = if (number > 0) Random.nextInt(0, number + 1) else 0

        val textView = findViewById<TextView>(R.id.text_random_number)
        textView.text = "$random"

        textView.setOnClickListener {
            val Intent = Intent()
            Intent.putExtra("random", random)
            setResult(RESULT_OK, Intent)
            finish()
        }

    }
}