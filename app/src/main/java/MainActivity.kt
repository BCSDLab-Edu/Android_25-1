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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Build
import android.content.pm.PackageManager
import android.content.SharedPreferences

class FirstActivity : AppCompatActivity() {
    var count = 0
    private lateinit var numTextView: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences("rand_prefs", MODE_PRIVATE)
        val mynotification = Notification(this)

        numTextView = findViewById(R.id.textview_num)

        val toastMessage = getString(R.string.toast_message)

        val toastButton: Button = findViewById(R.id.button_toast)
        val countButton: Button = findViewById(R.id.button_count)
        val randomButton: Button = findViewById(R.id.button_random)

        toastButton.setOnClickListener {
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        countButton.setOnClickListener {
            count++
            numTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            val bundle = bundleOf("currentCount" to count)
            mynotification.deliverNotification(bundle)
        }
    }
    override fun onResume() {
        super.onResume()
        val savedRandom = prefs.getInt("randomNumber",0)
            count = savedRandom
            numTextView.text = count.toString()
            prefs.edit().remove("randomNumber").apply()
    }
}
