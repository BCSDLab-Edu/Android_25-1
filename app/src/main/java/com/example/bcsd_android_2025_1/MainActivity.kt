package com.example.bcsd_android_2025_1
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NameAdapter
    private val nameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextView = findViewById<EditText>(R.id.edit_text)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val buttonAdd = findViewById<FloatingActionButton>(R.id.button_add)

        adapter = NameAdapter(nameList,
            onClick = { index ->
                AlertDialog.Builder(this)
                    .setTitle("삭제")
                    .setMessage("${nameList[index]}을(를) 삭제하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        nameList.removeAt(index)
                        adapter.notifyItemRemoved(index)
                    }
                    .setNegativeButton("취소", null)
                    .show() },
            onLongClick = { index ->
                val editText = EditText(this).apply {
                    setText(nameList[index])
                    setPadding(40, 20, 40, 20)
                }
                AlertDialog.Builder(this)
                    .setTitle("수정")
                    .setView(editText)
                    .setPositiveButton("확인") { _, _ ->
                        val newName = editText.text.toString().trim()
                        if (newName.isNotEmpty()) {
                            nameList[index] = newName
                            adapter.notifyItemChanged(index)
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        buttonAdd.setOnClickListener {
            val name = editTextView.text.toString().trim()
            if (name.isNotEmpty()) {
                nameList.add(name)
                adapter.notifyItemInserted(nameList.size - 1)
                editTextView.setText("")
            }
        }
    }
}