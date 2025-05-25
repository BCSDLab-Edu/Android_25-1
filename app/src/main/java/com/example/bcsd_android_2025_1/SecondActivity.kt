package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import kotlin.random.Random
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_login_second)
        setSupportActionBar(toolbar)

        NotificationManagerCompat.from(this).cancel(0)

        val number = intent.getIntExtra(getString(R.string.key_number), 0)

        val random = Random.nextInt(0, number + 1)

        val textView = findViewById<TextView>(R.id.text_random_number)
        textView.text = random.toString()

        textView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(getString(R.string.key_random), random)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}