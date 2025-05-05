    package com.example.bcsd_android_2025_1

    import android.annotation.SuppressLint
    import android.app.Activity
    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity

    class MainActivity : AppCompatActivity() {

        private var count = 0
        private lateinit var countTextView: TextView

        companion object {
            const val EXTRA_COUNT = "currentCount"
            const val EXTRA_RANDOM = "selectedNumber"
        }

        private val getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val newCount = data?.getIntExtra(EXTRA_RANDOM, count) ?: count
                count = newCount
                countTextView.text = count.toString()
            }
        }


        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_first)

            countTextView = findViewById(R.id.textViewCount)
            val toastButton = findViewById<Button>(R.id.toast)
            val countButton = findViewById<Button>(R.id.count)
            val randomButton = findViewById<Button>(R.id.random)

            toastButton.setOnClickListener {
                Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show()
            }

            countButton.setOnClickListener {
                count++
                countTextView.text = count.toString()
            }

            randomButton.setOnClickListener {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra(EXTRA_COUNT, count)
                getResult.launch(intent)
            }
        }


    }