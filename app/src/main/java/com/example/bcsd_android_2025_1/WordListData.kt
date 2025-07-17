package com.example.bcsd_android_2025_1

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "word_table")
data class WordListData (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var word: String,
    var meaning: String
)