
package com.example.bcsd_android_2025_1.data.repository

import androidx.lifecycle.LiveData
import com.example.bcsd_android_2025_1.data.model.Word
import com.example.bcsd_android_2025_1.data.room.WordDao

class WordRepository(private val wordDao: WordDao) {

    fun getAllWords(): LiveData<List<Word>> = wordDao.getAllWords()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    suspend fun update(word: Word) {
        wordDao.update(word)
    }

    suspend fun delete(word: Word) {
        wordDao.delete(word)
    }
}
