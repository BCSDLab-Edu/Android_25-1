package com.example.bcsd_android_2025_1
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val max = intent.getIntExtra("currentCount",0)
        val randomValue = Random.nextInt(0, max + 1)

        val resultText : TextView= findViewById(R.id.textview_random)
        resultText.text = "랜덤 숫자: $randomValue"

        lifecycleScope.launch {
            delay(1000L)
            val resultIntent = Intent()
            resultIntent.putExtra("randomNumber", randomValue)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}