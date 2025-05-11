package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.TextView
import android.app.Activity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import android.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var numberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(this, R.color.darkblue))
        )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        numberTextView = findViewById(R.id.textview_numberview)

        val toastButton: Button = findViewById(R.id.button_toast)
        val countButton: Button = findViewById(R.id.button_count)
        val randomButton: Button = findViewById(R.id.button_random)

        toastButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("버튼 띄우기")
                .setPositiveButton("Positive") { _, _ ->
                    count = 0
                    numberTextView.text = count.toString()
                }
                .setNeutralButton("Neutral") { _, _ ->
                    Toast.makeText(this, "neutral 버튼 클릭", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Negative", null)
                .show()
        }

        countButton.setOnClickListener {
            count++
            numberTextView.text = count.toString()
        }

        supportFragmentManager.setFragmentResultListener("randomResult", this) { _, bundle ->
            val randomNumber = bundle.getInt("randomNumber", count)
            count = randomNumber
            numberTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            val fragment = SecondFragment.newInstance(count)
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }



}