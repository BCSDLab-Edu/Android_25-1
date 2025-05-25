package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf

class MainActivity : AppCompatActivity() {
    companion object Constant {
        const val KEY_RANDOM_NUMBER: String = "keyRandomNumber"
        const val KEY_COUNT_NUMBER: String = "countNumber"
        const val CHANNEL_ID = "tralaChannel"
        const val NOTIFICATION_ID = 1
        const val KEY_NOTIFICATION = "keyNotification"
        const val NOTIFICATION_REQUEST_CODE = 100
        const val NOTIFICATION_DELETE_REQUEST_CODE = 101
    }

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonRandom: Button = findViewById(R.id.button_random)
        val buttonCount: Button = findViewById(R.id.button_count)
        val buttonToast: Button = findViewById(R.id.button_toast)
        val textviewNumber: TextView = findViewById(R.id.textview_number)
        val bigPictureBitmap = BitmapFactory.decodeResource(resources, R.drawable.tralalero_tralala)

        val toastMessage = getString(R.string.toast_message)

        createNotificationChannel()

        buttonToast.setOnClickListener {
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        buttonCount.setOnClickListener {
            count++
            textviewNumber.text = count.toString()
        }

        buttonRandom.setOnClickListener {
            val bundle = bundleOf(KEY_COUNT_NUMBER to count)
            val intent= Intent(this, SecondActivity::class.java).apply{
                putExtras(bundle)
            }
            val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT) //
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.tralalero_tralala)
                .setContentTitle(getString(R.string.text_notification_title))
                .setContentText(getString(R.string.text_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.tralalero_tralala, getString(R.string.text_more), pendingIntent)
                .addAction(R.drawable.tralalero_tralala, getString(R.string.text_close), getDeleteIntent())
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(bigPictureBitmap)
                    .bigLargeIcon(bigPictureBitmap)
                )
            with(NotificationManagerCompat.from(this)){
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.hasExtra(KEY_RANDOM_NUMBER)) {
            val randomNumber = intent.getIntExtra(KEY_RANDOM_NUMBER, 0)
            val textviewNumber: TextView = findViewById(R.id.textview_number)
            textviewNumber.text = randomNumber.toString()
            count = randomNumber
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.text_notification_channel_name)
            val descriptionText = getString(R.string.text_notification_channel_text)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply{
                description = descriptionText
            }
            val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getDeleteIntent(): PendingIntent {
        val deleteIntent = Intent(this, NotificationDeleteReceiver::class.java).apply {
            putExtra(KEY_NOTIFICATION, NOTIFICATION_ID)
        }
        return PendingIntent.getBroadcast(this, NOTIFICATION_DELETE_REQUEST_CODE, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}