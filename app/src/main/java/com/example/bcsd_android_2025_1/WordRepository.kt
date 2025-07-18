package com.example.bcsd_android_2025_1

import androidx.lifecycle.LiveData

class WordRepository(private val dao: WordDao) {
    val allWords : LiveData<List<Word>> = dao.getAllWords()

    suspend fun insert(word: Word) = dao.insert(word)
    suspend fun update(word: Word) = dao.update(word)
    suspend fun delete(word: Word) = dao.delete(word)
}