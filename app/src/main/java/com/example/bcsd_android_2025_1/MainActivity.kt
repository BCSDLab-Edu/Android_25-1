package com.example.bcsd_android_2025_1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "random_notification_channel"
        const val NOTIFICATION_ID = 1001
    }

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 알림 채널 생성
        createNotificationChannel()

        val countText: TextView = findViewById(R.id.textView)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val newNumber = data?.getIntExtra("COUNT", 0) ?: 0
                count = newNumber
                countText.text = count.toString()
            }
        }

        findViewById<Button>(R.id.button_toast).setOnClickListener {
            Toast.makeText(this, "Boink", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_count).setOnClickListener {
            count++
            countText.text = count.toString()
        }

        findViewById<Button>(R.id.button_random).setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {
        // SecondActivity로 이동 Intent
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("COUNT", count)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 닫기 버튼용 PendingIntent
        val closeIntent = Intent(this, NotificationDismissReceiver::class.java)
        val closePendingIntent = PendingIntent.getBroadcast(
            this, 0, closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 이미지 (drawable 예: R.drawable.notification_image)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_logo_kakao)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_kakao)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null as Bitmap?))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_logo_naver, getString(R.string.button_more), pendingIntent)
            .addAction(R.drawable.ic_logo_naver, getString(R.string.button_close), closePendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}