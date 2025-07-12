package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var timeTextView: TextView
    private lateinit var startPauseButton:Button
    private lateinit var stopButton:Button
    private lateinit var lapButton:Button
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView:RecyclerView

    private var isRunning = false
    private var startTime = 0L
    private var pauseOffset = 0L
    private var timerJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.time_textview)
        startPauseButton = findViewById(R.id.start_pause_toggle_button)
        stopButton = findViewById(R.id.stop_button)
        lapButton = findViewById(R.id.lap_button)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        startPauseButton.setOnClickListener {
            if(!isRunning){
                startPauseButton.text = getString(R.string.pause_text)
                startTime = SystemClock.elapsedRealtime() - pauseOffset
                startTimer()
                isRunning=true
            }else{
                startPauseButton.text=getString(R.string.start_text)
                pauseOffset = SystemClock.elapsedRealtime() - startTime
                stopTimer()
                isRunning=false
            }
        }

        stopButton.setOnClickListener {
            stopTimer()
            timeTextView.text = getString(R.string.default_time_text)
            startPauseButton.text = getString(R.string.start_text)
            isRunning = false
            pauseOffset = 0L
            recyclerViewAdapter.removeAllItems()
        }

        lapButton.setOnClickListener {
            if(isRunning){
                val elapsed = SystemClock.elapsedRealtime() - startTime
                val min = (elapsed / 1000) / 60
                val sec = (elapsed / 1000) % 60
                val mil = (elapsed % 1000) / 10
                val nowTime = getString(R.string.time_text, min, sec, mil)

                recyclerViewAdapter.addLapItem(nowTime)
                recyclerView.scrollToPosition(0)
            }
        }
    }

    private fun startTimer(){
        timerJob = lifecycleScope.launch{
            while(true){
                val elapsed = SystemClock.elapsedRealtime()-startTime
                val min = (elapsed/1000)/60
                val sec = (elapsed/1000)%60
                val mil = (elapsed%1000)/10
                timeTextView.text = getString(R.string.time_text, min, sec, mil)
                delay(10L)
            }
        }
    }

    private fun stopTimer(){
        timerJob?.cancel()
        timerJob = null
    }
}