package com.example.bcsd_android_2025_1

//import androidx.activity.result.contract.ActivityResultContracts
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var countData = 0


//    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            result ->
//        if (result.resultCode == RESULT_OK) {
//            val data = result.data
//            val randomNum = data?.getIntExtra("randomNum",-1) ?: -1
//
//            val tvResult: TextView = findViewById(R.id.tv_main_num)
//            tvResult.text = randomNum.toString()
//
//            countData = randomNum
//
//        }
//    }

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvResult: TextView = findViewById(R.id.tv_main_num)

        val btnCount: Button = findViewById(R.id.btn_main_count)


        val btnToast: Button = findViewById(R.id.btn_main_toast)

        btnToast.setOnClickListener {

            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setPositiveButton("Positive"){dialog,_ ->
                    countData = 0
                    tvResult.text = countData.toString()
                    dialog.dismiss()
                }
                .setNeutralButton("Neutral"){dialog,_->
                    Toast.makeText(this@MainActivity, R.string.btn_main_toast, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Negative"){dialog,_->
                    dialog.dismiss()
                }
                .create()
            dialog.show()

        }



        btnCount.setOnClickListener {
            countData += 1
            tvResult.text = countData.toString()
        }

        val btnRandom: Button = findViewById(R.id.btn_main_random)

        btnRandom.setOnClickListener {
            val fragment = RandomFragment.newInstance(countData)


            supportFragmentManager.beginTransaction()
                .replace(R.id.random_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }



    }
}
