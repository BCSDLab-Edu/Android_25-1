package com.example.bcsd_android_2025_1

import LapData
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var lapButton: Button
    private lateinit var lapRecyclerView: RecyclerView
    private lateinit var lapAdapter: LapAdapter
    private val lapList = mutableListOf<LapData>()

    private var isRunning = false
    private var startTime = 0L // 시작시간
    private var beforeTime = 0L // 누적시간
    private var timerJob: Job? = null // 중단 위함
    private var lastLapTime = 0L // 마지막 기록된 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = findViewById(R.id.main_text_timer)
        startButton = findViewById(R.id.main_button_start)
        stopButton = findViewById(R.id.main_button_stop)
        lapButton = findViewById(R.id.main_button_lap)
        lapRecyclerView = findViewById(R.id.main_recycler_lap)

        lapAdapter = LapAdapter(lapList)
        lapRecyclerView.adapter = lapAdapter
        lapRecyclerView.layoutManager = LinearLayoutManager(this)

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

        lapButton.setOnClickListener {
            if (!isRunning) return@setOnClickListener

            val now = SystemClock.elapsedRealtime()
            val total = now - startTime + beforeTime
            val section = if (lapList.isEmpty()) total else total - lastLapTime
            lastLapTime = total

            val lap = LapData(
                number = lapList.size + 1,
                sectionTime = formatTime(section),
                totalTime = formatTime(total)
            )

            lapList.add(0, lap)
            lapAdapter.notifyItemInserted(0)
            lapRecyclerView.scrollToPosition(0)
        }
    }

    private fun startTimer() {
        isRunning = true
        startButton.text = getString(R.string.main_pause)
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
        startButton.text = getString(R.string.main_start)
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
        lastLapTime = 0L
        timerText.text = getString(R.string.main_default_time)
        startButton.text = getString(R.string.main_start)

        lapList.clear()
        lapAdapter.notifyDataSetChanged()
    }
}