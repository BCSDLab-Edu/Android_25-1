package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<ListData>()
        list.add(ListData(getString(R.string.text_test_1)))
        list.add(ListData(getString(R.string.text_test_2)))
        list.add(ListData(getString(R.string.text_test_3)))

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        val adapter = RecyclerViewAdapter(list)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val editTextGetName:EditText = findViewById(R.id.edittext_get_name)
        val floatButton: FloatingActionButton = findViewById(R.id.floating_action_button)
        floatButton.setOnClickListener {
            list.add(ListData(editTextGetName.text.toString()))
            editTextGetName.setText("")
            adapter.notifyItemInserted(list.size-1) // 추가된 데이터 반영
        }
    }
}