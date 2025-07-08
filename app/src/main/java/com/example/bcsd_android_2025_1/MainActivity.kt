package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startPauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var lapButton: Button
    private lateinit var lapRecyclerView: RecyclerView
    private lateinit var lapAdapter: LapAdapter

    private var startTime = 0L
    private var elapsedTime = 0L
    private var isRunning = false
    private var timeJob: Job? = null

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

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
        startTime = SystemClock.elapsedRealtime() - elapsedTime
        isRunning = true
        startPauseButton.text = getString(R.string.text_pause)

        timeJob = coroutineScope.launch {
            while (isActive) {
                val elapsed = SystemClock.elapsedRealtime() - startTime
                timerTextView.text = nowTime(elapsed)
                delay(10L)
            }
        }
    }

    private fun pauseTimer() {
        elapsedTime = SystemClock.elapsedRealtime() - startTime
        timeJob?.cancel()
        isRunning = false
        startPauseButton.text = getString(R.string.text_start)
    }

    private fun stopTimer() {
        timeJob?.cancel()
        startTime = 0L
        elapsedTime = 0L
        isRunning = false
        timerTextView.text = getString(R.string.start_time)
        startPauseButton.text = getString(R.string.text_start)
        lapAdapter.clearLaps()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeJob?.cancel()
        coroutineScope.cancel()
    }
}