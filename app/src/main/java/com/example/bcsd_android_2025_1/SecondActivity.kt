package com.example.bcsd_android_2025_1


import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class SecondActivity : AppCompatActivity() {

    private var randomValue: Int = -1
    private var notificationId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("count_value", 0)
        notificationId = intent.getIntExtra("notification_id", -1)


        if (notificationId != -1) {
            NotificationManagerCompat.from(this).cancel(notificationId)
        }
        randomValue = (0 until count + 1).random()

        val textView = findViewById<TextView>(R.id.Count)
        textView.text = "$randomValue"
    }

    override fun onBackPressed() {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("random_value", randomValue)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        super.onBackPressed()
    }
}
