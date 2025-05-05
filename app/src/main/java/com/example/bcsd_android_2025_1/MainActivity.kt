package com.example.bcsd_android_2025_1
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var countText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countText = findViewById(R.id.text_view)

        findViewById<Button>(R.id.button_toast).setOnClickListener {
            Toast.makeText(this, "우진업!!!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_count).setOnClickListener {
            count++
            countText.text = count.toString()
        }

        findViewById<Button>(R.id.button_random).setOnClickListener {
            val random = Random()
            val random_num = random.nextInt(count+1)

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("CURRENT_COUNT", random_num.toString())
            startActivity(intent)
            count = random_num
            countText.text = random_num.toString()
        }


    }
}