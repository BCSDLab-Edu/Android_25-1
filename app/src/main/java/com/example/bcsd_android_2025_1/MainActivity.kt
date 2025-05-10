package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment = MainFragment().apply{
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_main_fragment, mainFragment, "MAIN_FRAGMENT_TAG")
            .commit()
    }
}