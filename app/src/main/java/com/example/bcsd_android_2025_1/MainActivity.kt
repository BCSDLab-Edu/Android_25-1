package com.example.bcsd_android_2025_1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerViewAdapter
    private val nameList = mutableListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclerview)


        val input = findViewById<EditText>(R.id.input_name)
        val add = findViewById<FloatingActionButton>(R.id.add_name)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_name)

        adapter = RecyclerViewAdapter(this, nameList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        add.setOnClickListener {
            val name = input.text.toString().trim()
            if (name.isNotEmpty()) {
                adapter.add_name(name)
                input.text.clear()
            }
        }
    }
}
