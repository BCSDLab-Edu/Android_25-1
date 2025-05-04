package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.util.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_random: Button = findViewById(R.id.button_random)
        val button_count: Button = findViewById(R.id.button_count)
        val button_toast: Button = findViewById(R.id.button_toast)

        val textview_number: TextView = findViewById((R.id.textview_number))

        var count = 0

        button_toast.setOnClickListener {
            Toast.makeText(this, "피슝파슝푸슝", Toast.LENGTH_SHORT).show()
        }

        button_count.setOnClickListener {
            count++
            textview_number.text = count.toString()
        }

        button_random.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            val random_number = Random().nextInt(count+1)
            intent.putExtra("count_number", random_number.toString())
            startActivity(intent)  // 화면 전환을 시켜줌
            count = random_number
            textview_number.text = random_number.toString()
        }
    }
}