package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextName: EditText = findViewById(R.id.text_main_name)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_main_list)

        val recyclerViewAdapter = RecyclerViewAdapter()

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }


        val fab: FloatingActionButton = findViewById(R.id.button_main_fab)
        fab.setOnClickListener {
            val name = editTextName.text.toString().trim()
            if (name.isNotEmpty()) {
                recyclerViewAdapter.addItem(ListData(name))
                editTextName.setText("")
                recyclerView.scrollToPosition(recyclerViewAdapter.itemCount - 1)
            }
        }
    }
}