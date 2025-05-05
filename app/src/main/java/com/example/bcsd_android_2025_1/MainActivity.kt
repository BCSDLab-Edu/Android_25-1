package com.example.bcsd_android_2025_1


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var count = 0

        val countButton = findViewById<Button>(R.id.CountButton)
        val countText = findViewById<TextView>(R.id.Count)
        val toastButton = findViewById<Button>(R.id.ToastButton)
        val randomButton = findViewById<Button>(R.id.RandomButton)

        toastButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "BCSD 화이팅", Toast.LENGTH_SHORT).show()
        }
        countButton.setOnClickListener {
            count++
            countText.text = count.toString()
        }


        randomButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            val randomValue = (0 until count).random()
            count = randomValue
            countText.text = count.toString()
            intent.putExtra("count_value", count)
            startActivity(intent)
        }


    }
}