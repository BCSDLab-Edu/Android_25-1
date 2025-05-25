package com.example.bcsd_android_2025_1

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.TextView
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.drawable.ColorDrawable

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "random_notification_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_CLOSE_NOTIFICATION = "ACTION_CLOSE_NOTIFICATION"
    }

    private var count = 0
    private lateinit var numberTextView: TextView

    private val randomNumberLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newCount = result.data?.getIntExtra("randomNumber", count)
            if (newCount != null) {
                count = newCount
                numberTextView.text = count.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        requestNotificationPermissionIfNeeded()

        numberTextView = findViewById(R.id.textview_numberview)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(this, R.color.darkblue))
        )

        createNotificationChannel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toastButton: Button = findViewById(R.id.button_toast)
        val countButton: Button = findViewById(R.id.button_count)
        val randomButton: Button = findViewById(R.id.button_random)

        toastButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show()
        }

        countButton.setOnClickListener {
            count++
            numberTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            showNotification(count)

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("count", count)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == ACTION_CLOSE_NOTIFICATION) {
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun showNotification(count: Int) {
        val resultIntent = Intent(this, SecondActivity::class.java).apply {
            putExtra("count", count)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_CLOSE_NOTIFICATION
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val closePendingIntent = PendingIntent.getActivity(
            this, 1, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeImage = BitmapFactory.decodeResource(resources, R.drawable.notification_image)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(largeImage)
                    .bigLargeIcon(null as Bitmap?)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                getString(R.string.action_more),
                resultPendingIntent
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                getString(R.string.action_close),
                closePendingIntent
            )

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
