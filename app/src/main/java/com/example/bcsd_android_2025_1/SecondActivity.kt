package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("count_value",0)

        val textView = findViewById<TextView>(R.id.Count)

        textView.text = count.toString()


    }

}