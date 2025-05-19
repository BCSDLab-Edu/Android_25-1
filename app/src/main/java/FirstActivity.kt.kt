package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

class FirstActivity : AppCompatActivity() {

    private lateinit var adapter: NameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.editText_Name)
        val addButton: FloatingActionButton = findViewById(R.id.button_Add)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        adapter = NameAdapter(
            onClick = { position -> showDeleteDialog(position) },
            onLongClick = { position -> showEditDialog(position) })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val name = editText.text.toString()
            if (name.isNotBlank()) {
                adapter.addItem(name)
                editText.text.clear()
            }
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_title)
            .setMessage(R.string.delete_message)
            .setPositiveButton(R.string.text_delete) { _, _ ->
                adapter.removeItem(position)
            }
            .setNegativeButton(R.string.text_cancel, null)
            .show()
    }

    private fun showEditDialog(position: Int) {
        val editText = EditText(this)
        val currentName = adapter.getItem(position)
        editText.setText(currentName)

        AlertDialog.Builder(this)
            .setTitle(R.string.edit_title)
            .setView(editText)
            .setPositiveButton(R.string.text_okay) { _, _ ->
                val newName = editText.text.toString()
                adapter.editItem(currentName, newName)
            }
            .setNegativeButton(R.string.text_cancel, null)
            .show()
    }
}