    package com.example.bcsd_android_2025_1

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.os.bundleOf

    class MainActivity : AppCompatActivity() {

        private val startActivityWithResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val randomNum = data?.getIntExtra("randomNum",-1) ?: -1

                val tvResult: TextView = findViewById(R.id.tv_main_num)
                tvResult.text = randomNum.toString()

            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)


            val btnToast: Button = findViewById(R.id.btn_main_toast)

            btnToast.setOnClickListener {
                Toast.makeText(this@MainActivity, R.string.btn_main_toast, Toast.LENGTH_SHORT).show()
            }

            val tvResult: TextView = findViewById(R.id.tv_main_num)
            var countData = 0
            val btnCount: Button = findViewById(R.id.btn_main_count)

            btnCount.setOnClickListener {
                countData += 1
                tvResult.text = countData.toString()
            }

            val btnRandom: Button = findViewById(R.id.btn_main_random)

            btnRandom.setOnClickListener {
                val randomBundle = bundleOf("randomData" to countData)
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtras(randomBundle)
                startActivityWithResult.launch(intent)
            }



        }
    }
