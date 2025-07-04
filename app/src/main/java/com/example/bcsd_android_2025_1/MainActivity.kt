package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startPauseButton: Button
    private lateinit var stopButton: Button

    private lateinit var lapButton: Button
    private lateinit var lapRecyclerView: RecyclerView
    private lateinit var lapAdapter: LapAdapter

    private var isRunning = false
    private var startTime = 0L
    private var pauseOffset = 0L
    private val handler = Handler(Looper.getMainLooper())


    private val updateRunnable = object : Runnable {
        override fun run() {
            val elapsed = System.currentTimeMillis() - startTime
            val minutes = (elapsed / 1000) / 60
            val seconds = (elapsed / 1000) % 60
            val milliseconds = (elapsed % 1000) / 10

            val timeString = getString(R.string.formatted_timer, minutes, seconds, milliseconds)
            timerTextView.text = timeString

            handler.postDelayed(this, 10)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timerTextView = findViewById(R.id.timer_textview)
        startPauseButton = findViewById(R.id.start_pause_button)
        stopButton = findViewById(R.id.stop_button)

        val defaultTimeString = getString(R.string.default_timer_text)

        startPauseButton.setOnClickListener {
            if (!isRunning) {
                startTime = System.currentTimeMillis() - pauseOffset
                handler.post(updateRunnable)
                isRunning = true
                startPauseButton.text = getString(R.string.button_pause)
            } else {
                pauseOffset = System.currentTimeMillis() - startTime
                handler.removeCallbacks(updateRunnable)
                isRunning = false
                startPauseButton.text = getString(R.string.button_start)
            }
        }

        stopButton.setOnClickListener {
            handler.removeCallbacks(updateRunnable)
            isRunning = false
            pauseOffset = 0L
            timerTextView.text = getString(R.string.default_timer_text)
            startPauseButton.text = getString(R.string.button_start)
            lapAdapter.clear()
        }

        lapButton = findViewById(R.id.lap_button)
        lapRecyclerView = findViewById(R.id.lap_recyclerview)
        lapAdapter = LapAdapter()

        lapRecyclerView.apply {
            adapter = lapAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        lapButton.setOnClickListener {
            if (isRunning) {
                val elapsed = System.currentTimeMillis() - startTime
                val minutes = (elapsed / 1000) / 60
                val seconds = (elapsed / 1000) % 60
                val milliseconds = (elapsed % 1000) / 10

                val lapTime = getString(R.string.formatted_timer, minutes, seconds, milliseconds)
                lapAdapter.addLap(lapTime)
                lapRecyclerView.scrollToPosition(0)
            }
        }
    }
}