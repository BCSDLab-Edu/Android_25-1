package com.example.bcsd_android_2025_1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "channel_id"

class MainActivity : AppCompatActivity() {

    private var countData = 0

    private val  startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val randomNum = data?.getIntExtra("randomNum",-1) ?: -1

            val tvResult: TextView = findViewById(R.id.tv_main_num)
            tvResult.text = randomNum.toString()

            countData = randomNum

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleIntent(intent)


        val btnToast: Button = findViewById(R.id.btn_main_toast)

        btnToast.setOnClickListener {
            Toast.makeText(this@MainActivity, R.string.btn_main_toast, Toast.LENGTH_SHORT).show()
        }

        val tvResult: TextView = findViewById(R.id.tv_main_num)

        val btnCount: Button = findViewById(R.id.btn_main_count)

        btnCount.setOnClickListener {
            countData += 1
            tvResult.text = countData.toString()
        }



        val btnRandom: Button = findViewById(R.id.btn_main_random)


        btnRandom.setOnClickListener {
            val name = getString(R.string.notification_name)
            val descriptionText = getString(R.string.notification_descriptionText)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val moreIntent = Intent(this,MainActivity::class.java).apply{
                putExtra("fromNotification",true)
                putExtra("randomData",countData)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val morePendingIntent = PendingIntent.getActivity(
                this, 0, moreIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )


            val closeIntent = Intent(this, NotificationReceiver::class.java).apply{
                action = "ACTION_CLOSE_NOTIFICATION"
            }
            val closePendingIntent = PendingIntent.getBroadcast(
                this,0,closeIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val bigPicture: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.notification_bigpicture)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(0,getString(R.string.notification_seemore),morePendingIntent)
                .addAction(0,getString(R.string.notification_close),closePendingIntent)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .bigLargeIcon(null as Bitmap?)
                )

            notificationManager.notify(1001, builder.build())
        }



    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val fromNotification = intent.getBooleanExtra("fromNotification",false)
        val randomData = intent.getIntExtra("randomData",10)

        if (fromNotification) {

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(1001)

            val secondIntent = Intent(this, SecondActivity::class.java)
            secondIntent.putExtra("randomData",randomData)
            startForResult.launch(secondIntent)
        }
    }
}

