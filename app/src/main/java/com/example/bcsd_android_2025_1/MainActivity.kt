package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startPauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var lapButton: Button
    private lateinit var lapRecyclerView: RecyclerView
    private lateinit var lapAdapter: LapAdapter

    private var startTime = 0L
    private var pauseOffset = 0L
    private var isRunning = false
    private var handler = Handler(Looper.getMainLooper())

    private val updateTimer = object : Runnable {
        override fun run() {
            val elapsed = SystemClock.elapsedRealtime() - startTime
            timerTextView.text = nowTime(elapsed)
            handler.postDelayed(this, 10)
        }
    }
    private fun nowTime(ms: Long): String {
        val minutes = (ms / 60000).toInt()
        val seconds = ((ms % 60000) / 1000).toInt()
        val millseconds = ((ms % 1000) / 10).toInt()
        val timeString = getString(R.string.now_time, minutes, seconds, millseconds)
        return timeString
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.textview_timer)
        startPauseButton = findViewById(R.id.button_start_pause)
        stopButton = findViewById(R.id.button_stop)
        lapButton = findViewById(R.id.button_lap)
        lapRecyclerView = findViewById(R.id.lap_recyclerview)
        lapAdapter = LapAdapter()

        lapRecyclerView.adapter = lapAdapter
        lapRecyclerView.layoutManager = LinearLayoutManager(this)

        startPauseButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
        stopButton.setOnClickListener {
            stopTimer()
        }
        lapButton.setOnClickListener {
            if (isRunning) {
                val currentTime = SystemClock.elapsedRealtime() - startTime
                val lapTime = nowTime(currentTime)

                lapAdapter.addLap(lapTime)
                lapRecyclerView.scrollToPosition(0)
            }
        }
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime() - pauseOffset
        handler.post(updateTimer)
        isRunning = true
        startPauseButton.text = getString(R.string.text_pause)
    }

    private fun pauseTimer() {
        pauseOffset = SystemClock.elapsedRealtime() - startTime
        handler.removeCallbacks(updateTimer)
        isRunning = false
        startPauseButton.text = getString(R.string.text_start)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateTimer)
        startTime = 0L
        pauseOffset = 0L
        isRunning = false
        timerTextView.text = getString(R.string.start_time)
        startPauseButton.text = getString(R.string.text_start)
        lapAdapter.clearLaps()
    }
}