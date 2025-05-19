package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namelistapp.NameAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.DividerItemDecoration

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NameAdapter

    private val names = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        editTextName = findViewById(R.id.input_field)
        btnAdd = findViewById(R.id.btn_add)
        recyclerView = findViewById(R.id.recyclerview)

        adapter = NameAdapter(names,
            onItemClicked = { position -> showDeleteDialog(position) },
            onItemLongClicked = { position -> showEditDialog(position) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        btnAdd.setOnClickListener {
            val input = editTextName.text.toString().trim()
            if (input.isNotEmpty()) {
                names.add(input)
                adapter.notifyItemInserted(names.size - 1)
                editTextName.text.clear()
            }
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setMessage("정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                names.removeAt(position)
                adapter.notifyItemRemoved(position)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditDialog(position: Int) {
        val editText = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(names[position])
            setSelection(text.length)
        }

        AlertDialog.Builder(this)
            .setTitle("이름 수정")
            .setView(editText)
            .setPositiveButton("확인") { dialog, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    names[position] = newName
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
