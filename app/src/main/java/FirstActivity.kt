package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

class FirstActivity : AppCompatActivity() {
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numTextView: TextView = findViewById(R.id.textview_num)
        val toastButton: Button = findViewById(R.id.button_toast)
        val countButton: Button = findViewById(R.id.button_count)
        val randomButton: Button = findViewById(R.id.button_random)

        val toastMessage = getString(R.string.toast_message)



        val startActivityWithResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val bundle = result.data?.extras
                count = bundle?.getInt("randomNumber", count) ?: count
                numTextView.text = count.toString()
            }
        }

        toastButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("프로미스나인")
                .setMessage("선택ㄱㄱ")
                .setPositiveButton("초기화ㄱ") { dialog, which ->
                    count = 0
                    numTextView.text = count.toString()
                }
                .setNeutralButton("메시지ㄱ") { dialog, which ->
                    Toast.makeText(this,toastMessage, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("종료ㄱ") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }


        countButton.setOnClickListener {
            count++
            numTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_zone,Fragment1())
                .addToBackStack(null)
                .commit()
            //val bundle = bundleOf("currentCount" to count)
            //val intent = Intent(this, Fragment1::class.java)
            //intent.putExtras(bundle)
            //startActivityWithResult.launch(intent)
        }
    }
}