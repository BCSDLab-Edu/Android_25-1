package com.example.bcsd_android_2025_1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private var number = 0
    lateinit var textView: TextView

    private val getRandomNumber =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val randomNumber = data?.getIntExtra(getString(R.string.key_random), number)
                if (randomNumber != null) {
                    number = randomNumber
                    textView.text = number.toString()
                }
            }
        }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_login_main)
        setSupportActionBar(toolbar)

        val toastButton = findViewById<Button>(R.id.button_login_toast)
        toastButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.main_toastMassage), Toast.LENGTH_SHORT).show()
        }

        textView = findViewById(R.id.text_login_number)
        val countButton = findViewById<Button>(R.id.button_login_count)
        countButton.setOnClickListener {
            number++
            textView.text = number.toString()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        val randomButton = findViewById<Button>(R.id.button_login_random)
        randomButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val CHANNEL_ID = "randomChannel"
                val name = getString(R.string.notification_random)
                val descriptionText = getString(R.string.notification_random_text)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                val addIntent = Intent(this, SecondActivity::class.java)
                addIntent.putExtra(getString(R.string.key_number), number)
                val addPendingIntent = PendingIntent.getActivity(this, 0, addIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                val closeIntent = Intent()
                val closePendingIntent = PendingIntent.getActivity(this, 1, closeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                val image = BitmapFactory.decodeResource(resources, R.drawable.notification_content)

                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(getString(R.string.notification_textTitle))
                    .setContentText(getString(R.string.notification_textContent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(addPendingIntent)
                    .setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(image)
                            .bigLargeIcon(null as Bitmap?)
                    )
                    .addAction(0, getString(R.string.notification_add), addPendingIntent)
                    .addAction(0, getString(R.string.notification_close), closePendingIntent)

                with(NotificationManagerCompat.from(this)) {
                    notify(0, builder.build())
                }
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val randomNumber = intent.getIntExtra(getString(R.string.key_random), -1)
        if (randomNumber != -1) {
            number = randomNumber
            if (this::textView.isInitialized) {
                textView.text = number.toString()
            }
        }
    }
}

