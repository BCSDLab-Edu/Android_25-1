package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

class FirstActivity : AppCompatActivity() {
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numTextView: TextView = findViewById(R.id.textview_num)
        val toastButton: Button = findViewById(R.id.button_toast)
        val countButton: Button = findViewById(R.id.button_count)
        val randomButton: Button = findViewById(R.id.button_random)

        val toastMessage = getString(R.string.toast_message)

        val startActivityWithResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val bundle = result.data?.extras
                count = bundle?.getInt("randomNumber", count) ?: count
                numTextView.text = count.toString()
            }
        }

        toastButton.setOnClickListener {
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        countButton.setOnClickListener {
            count++
            numTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            val bundle = bundleOf("currentCount" to count)
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtras(bundle)
            startActivityWithResult.launch(intent)
        }
    }
}