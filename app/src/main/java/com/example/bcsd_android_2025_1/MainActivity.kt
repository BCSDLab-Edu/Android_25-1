package com.example.bcsd_android_2025_1
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
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

        val startActivityWithResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.hasExtra("random_value")) {
                    val randomValue = data.getIntExtra("random_value", 0)
                    count = randomValue
                    countText.text = count.toString()
                }
            }
        }

        findViewById<Button>(R.id.button_random).setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.putExtra("count_value", count)
            startActivityWithResult.launch(intent)
        }
    }
}