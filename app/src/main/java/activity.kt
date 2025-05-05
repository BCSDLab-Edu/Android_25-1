package com.example.bcsd_android_2025_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Activity : AppCompatActivity() {
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val num_textview : TextView = findViewById(R.id.textview_num)
        val toast_button : Button = findViewById(R.id.button_toast)
        val count_button : Button = findViewById(R.id.button_count)
        val random_button : Button = findViewById(R.id.button_random)

        toast_button.setOnClickListener {
            Toast.makeText(this, "상진인 애기야!", Toast.LENGTH_SHORT).show()
        }

        count_button.setOnClickListener {
            count++
            num_textview.text = count.toString()
        }

        random_button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("currentCount", count)
            startActivityForResult(intent, 7777)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7777 && resultCode == RESULT_OK) {
            val result = data?.getIntExtra("randomNumber", count) ?: count
            count = result
            val numTextView: TextView = findViewById(R.id.textview_num)
            numTextView.text = count.toString()
        }
    }
}