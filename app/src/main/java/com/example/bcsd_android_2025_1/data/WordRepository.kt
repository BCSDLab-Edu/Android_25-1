package com.example.bcsd_android_2025_1

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao) {
    val allWords: LiveData<List<WordListData>> = wordDao.getAllwords()

    suspend fun insert(word: WordListData) = wordDao.insert(word)
    suspend fun update(word: WordListData) = wordDao.update(word)
    suspend fun delete(word: WordListData) = wordDao.delete(word)
}