package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private var isRunning = false
    private var startTime = 0L // 시작시간
    private var beforeTime = 0L // 누적시간
    private var timerJob: Job? = null // 중단 위함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = findViewById(R.id.main_text_timer)
        startButton = findViewById(R.id.main_button_start)
        stopButton = findViewById(R.id.main_button_stop)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTimer()
            } else {
                pauseTimer()
            }
        }

        stopButton.setOnClickListener {
            stopTimer()
        }
    }

    private fun startTimer() {
        isRunning = true
        startButton.text = "Pause"
        startTime = SystemClock.elapsedRealtime() // 시작시점

        timerJob = lifecycleScope.launch {
            while (isRunning) {
                val now = SystemClock.elapsedRealtime()
                val totalTime = now - startTime + beforeTime  // 총 누적 시간 = 현재 시간 - 시작시간 + 이전누적시간
                timerText.text = formatTime(totalTime)
                delay(10)  // 10ms마다 갱신
            }
        }
    }

    private fun pauseTimer() {
        isRunning = false
        beforeTime += SystemClock.elapsedRealtime() - startTime  // 이전 누적 시간에 이번에 흐른 시간 더함.
        timerJob?.cancel()
        startButton.text = "Start"
    }

    private fun formatTime(ms: Long): String {
        val minutes = ms / 1000 / 60
        val seconds = (ms / 1000) % 60
        val millis = (ms % 1000) / 10
        return String.format("%02d : %02d : %02d", minutes, seconds, millis)
    }

    private fun stopTimer() {
        isRunning = false
        timerJob?.cancel() // 코루틴 정지 + 기록 초기화
        startTime = 0L
        beforeTime = 0L
        timerText.text = "00 : 00 : 00"
        startButton.text = "Start"
    }
}