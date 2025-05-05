package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    var number = 0
    lateinit var textView: TextView

    private val getRandomNumber =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val randomNumber = data?.getIntExtra("random", number)
                if (randomNumber != null) {
                    number = randomNumber
                    textView.text = number.toString()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_login_main)
        setSupportActionBar(toolbar)

        val toastButton = findViewById<Button>(R.id.button_login_toast)
        toastButton.setOnClickListener {
            Toast.makeText(this, "와우!!", Toast.LENGTH_SHORT).show()
        }

        textView = findViewById(R.id.text_login_number)
        val countButton = findViewById<Button>(R.id.button_login_count)
        countButton.setOnClickListener {
            number++
            textView.text = number.toString()
        }

        val randomButton = findViewById<Button>(R.id.button_login_random)
        randomButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("number", number)
            getRandomNumber.launch(intent)
        }

    }

}