package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var nameList: MutableList<String>
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameList = mutableListOf()

        adapter = Adapter(
            nameList,
            onClick = { pos -> deleteDialog(pos) },
            onLongClick = { pos -> editDialog(pos) }
        )

        val editText = findViewById<EditText>(R.id.editTextName)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val buttonAdd = findViewById<FloatingActionButton>(R.id.buttonAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        buttonAdd.setOnClickListener {
            val name = editText.text.toString()
            nameList.add(name)
            adapter.notifyItemInserted(nameList.size - 1)
            recyclerView.scrollToPosition(nameList.size - 1)
            editText.text.clear()
        }
    }

    private fun deleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("이름 목록 삭제하기")
            .setMessage("이름 목록을 삭제해보자.")
            .setPositiveButton("삭제") { dialog, _ ->
                nameList.removeAt(position)
                adapter.notifyItemRemoved(position)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun editDialog(pos: Int) {
        val input = EditText(this).apply {
            setText(nameList[pos])
        }

        AlertDialog.Builder(this)
            .setTitle("이름 편집")
            .setView(input)
            .setPositiveButton("확인") { dialog, _ ->
                val newName = input.text.toString()
                nameList[pos] = newName
                adapter.notifyItemChanged(pos)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}