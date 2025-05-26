package com.example.bcsd_android_2025_1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


const val NOTIFICATION_CHANNEL_ID = "random_channel_id"
const val NOTIFICATION_ID = 1001
const val CLOSE_NOTIFICATION = "CLOSE_NOTIFICATION_ACTION"

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var countText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countText = findViewById(R.id.Count)
        val countButton = findViewById<Button>(R.id.CountButton)
        val toastButton = findViewById<Button>(R.id.ToastButton)
        val randomButton = findViewById<Button>(R.id.RandomButton)

        countButton.setOnClickListener {
            count++
            countText.text = count.toString()
        }

        toastButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show()
        }

        randomButton.setOnClickListener {
            createNotificationChannel()
            showNotification()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        if (intent.action == CLOSE_NOTIFICATION) {
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
            return
        }

        val random = intent.getIntExtra("random_value", -1)
        if (random != -1) {
            count = random
            countText.text = count.toString()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.channel_description)
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun getDeleteIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = CLOSE_NOTIFICATION
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            this,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification() {
        val contentIntent = Intent(this, SecondActivity::class.java).apply {
            putExtra("count_value", count)
            putExtra("notification_id", NOTIFICATION_ID)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingContentIntent = PendingIntent.getActivity(
            this,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.fr)
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.fr)
            .setContentTitle(getString(R.string.random_check))
            .setContentText(getString(R.string.button_check))
            .setContentIntent(pendingContentIntent)
            .addAction(
                R.drawable.fr,
                getString(R.string.more),
                pendingContentIntent
            ) .setAutoCancel(true)
            .addAction(
                R.drawable.fr,
                getString(R.string.text_close),
                getDeleteIntent()
            )
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(largeImageBitmap)
                .bigLargeIcon(largeImageBitmap)
            )

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}