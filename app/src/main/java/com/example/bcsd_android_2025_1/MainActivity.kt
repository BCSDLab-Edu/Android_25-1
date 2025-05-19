package com.example.bcsd_android_2025_1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private val itemList = mutableListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.et_main_input)
        val buttonPlus = findViewById<FloatingActionButton>(R.id.fab_main_plus)
        recyclerView = findViewById(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MyAdapter(itemList,
            onDelete = { position -> showDeleteDialog(position) },
            onEdit = { position -> showEditDialog(position) }
        )

        recyclerView.adapter = adapter

        buttonPlus.setOnClickListener {
            val text = nameInput.text.toString()
            if (text.isNotBlank()) {
                itemList.add(text)
                adapter.notifyItemInserted(itemList.size - 1)
                nameInput.text.clear()
            }
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("이름 목록 삭제하기")
            .setMessage("이름 목록을 삭제해보자.")
            .setPositiveButton("삭제") { dialog, _ ->
                itemList.removeAt(position)
                adapter.notifyItemRemoved(position)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditDialog(position: Int) {
        val editText = EditText(this)
        editText.setText(itemList[position])

        AlertDialog.Builder(this)
            .setTitle("수정할 이름을 입력하세요")
            .setView(editText)
            .setPositiveButton("확인") { dialog, _ ->
                val newText = editText.text.toString()
                if (newText.isNotBlank()) {
                    itemList[position] = newText
                    adapter.notifyItemChanged(position)
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}