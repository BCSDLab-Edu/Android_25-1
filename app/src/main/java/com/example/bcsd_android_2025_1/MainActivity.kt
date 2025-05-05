package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonRandom: Button = findViewById(R.id.button_random)
        val buttonCount: Button = findViewById(R.id.button_count)
        val buttonToast: Button = findViewById(R.id.button_toast)

        val textviewNumber: TextView = findViewById(R.id.textview_number)

        var count = 0

        val toastMessage = getString(R.string.toast_message)

        val startActivityWithResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val bundle = result.data?.extras
                count = bundle?.getInt("keyRandomNumber", 0 )!!
                //count = result.data?.getIntExtra("keyRandomNumber", 0)!!
                textviewNumber.text = count.toString()
            }
        }

        buttonToast.setOnClickListener {
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        buttonCount.setOnClickListener {
            count++
            textviewNumber.text = count.toString()
        }

        buttonRandom.setOnClickListener {
            val bundle = bundleOf("countNumber" to count)
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtras(bundle)
            startActivityWithResult.launch(intent)
        }
    }


}