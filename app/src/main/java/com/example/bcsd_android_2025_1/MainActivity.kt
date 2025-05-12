package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val firstFragment = FirstFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, firstFragment)
                .commit()
        }
    }
}
