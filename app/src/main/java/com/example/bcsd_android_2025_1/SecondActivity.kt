package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val randomData = intent.getIntExtra("randomData", -1)

        val randomNum = (0..randomData).random()

        val tvResult: TextView = findViewById(R.id.tv_main2_num)
        tvResult.text = randomNum.toString()



        val intent = Intent(this, MainActivity::class.java).apply{

            putExtra("randomNum", randomNum)
        }
        setResult(RESULT_OK, intent)
    }
}