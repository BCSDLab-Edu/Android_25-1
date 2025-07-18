package com.example.bcsd_android_2025_1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "word_table")

data class Word(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val word: String,
    val meaning: String
) : Serializable