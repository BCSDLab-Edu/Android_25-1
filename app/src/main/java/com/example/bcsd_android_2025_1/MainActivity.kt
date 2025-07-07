package com.example.bcsd_android_2025_1
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer : TextView
    private lateinit var btnLap : Button
    private lateinit var btnStart : Button
    private lateinit var btnStop : Button

    private lateinit var rvRecord : RecyclerView
    private lateinit var lapAdapter: LapAdapter

    private var isRunning = false
    private var runningTime = 0L
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tv_main_timer)
        btnLap = findViewById(R.id.btn_main_lap)
        btnStart = findViewById(R.id.btn_main_start)
        btnStop  = findViewById(R.id.btn_main_stop)
        rvRecord = findViewById(R.id.rv_main_record)
        rvRecord.layoutManager = LinearLayoutManager(this)
        lapAdapter = LapAdapter()
        rvRecord.adapter = lapAdapter

        btnStart.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        btnStop.setOnClickListener {
            stopTimer()
        }

        btnLap.setOnClickListener {
            if (isRunning) {
                lapAdapter.addLap(formatTime(runningTime))
            }
        }
    }

    private fun startTimer() {
        val startTime = System.currentTimeMillis() - runningTime
        job = lifecycleScope.launch {
            while (isActive) {
                runningTime = System.currentTimeMillis() - startTime
                tvTimer.text = formatTime(runningTime)
                delay(10)
            }
        }
        isRunning = true
        btnStart.text = getString(R.string.btn_pause)
    }

    private fun pauseTimer() {
        job?.cancel()
        isRunning = false
        btnStart.text = getString(R.string.btn_start)
    }

    private fun stopTimer() {
        job?.cancel()
        runningTime = 0L
        isRunning = false
        tvTimer.text = getString(R.string.tv_time)
        btnStart.text = getString(R.string.btn_start)
        lapAdapter.clear()
    }

    private fun formatTime(ms: Long): String {
        val min = (ms/1000)/60
        val sec = (ms/1000)%60
        val millisec = (ms%1000) / 10
        return String.format("%02d:%02d:%02d", min, sec, millisec)
    }


}

