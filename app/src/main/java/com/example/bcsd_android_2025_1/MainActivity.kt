    package com.example.bcsd_android_2025_1

    import android.annotation.SuppressLint
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.Context
    import android.content.Intent
    import android.graphics.BitmapFactory
    import android.os.Bundle
    import android.widget.Button
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat

    class MainActivity : AppCompatActivity() {

        private var count = 0
        private lateinit var countTextView: TextView

        companion object {
            const val channelID = "channel_id"
            const val EXTRA_COUNT = "currentCount"
            const val notificationId = 1002
            const val REQUEST_CODE = 2001
        }

        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_first)

            countTextView = findViewById(R.id.textViewCount)
            val toastButton = findViewById<Button>(R.id.toast)
            val countButton = findViewById<Button>(R.id.count)
            val randomButton = findViewById<Button>(R.id.random)

            createNotificationChannel()


            toastButton.setOnClickListener {
                Toast.makeText(this, "Frank Ocean", Toast.LENGTH_SHORT).show()
            }

            countButton.setOnClickListener {
                count++
                countTextView.text = count.toString()
            }

            randomButton.setOnClickListener {

                initNotification()


            }
        }

        override fun onResume() {
            super.onResume()
            val prefs = getSharedPreferences("prefer", MODE_PRIVATE)
            val savedRandom = prefs.getInt("new_random", count)
            if (savedRandom != count) {
                count = savedRandom
                countTextView.text = count.toString()
                prefs.edit().remove("new_random").apply()
            }
        }


        @SuppressLint("MissingPermission")
        private fun initNotification() {

            val image = BitmapFactory.decodeResource(resources, R.drawable.ic_my_photo)

            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(EXTRA_COUNT, count)
            }

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val deleteIntent = Intent(this, NotificationReceiver::class.java)

            val deletePendingIntent =
                PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_IMMUTABLE)


            var builder = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_spotify)
                .setContentTitle(getString(R.string.text_title))
                .setContentText(getString(R.string.textContent))
                .setLargeIcon(image)
                .addAction(R.drawable.ic_launcher_background, getString(R.string.more), pendingIntent)
                .addAction(
                    R.drawable.ic_launcher_background,
                    getString(R.string.back),
                    deletePendingIntent
                )

                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        }

        private fun createNotificationChannel() {

            val name = getString(R.string.notification_channel_random)

            val descriptionText = getString(R.string.channel_text)

            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }



    }