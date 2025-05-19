package com.example.bcsd_android_2025_1
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NameAdapter
    private val itemList = mutableListOf<ListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = NameAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val editText = findViewById<EditText>(R.id.edit_text)
        val addButton = findViewById<Button>(R.id.button_add)

        addButton.setOnClickListener {
            val name = editText.text.toString()
            if (name.isNotBlank()) {
                itemList.add(ListItem(name))
                adapter.notifyItemInserted(itemList.size - 1)
                editText.text.clear()
            }
        }
    }

    data class ListItem(val name: String)
}
