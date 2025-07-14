package com.example.bcsd_android_2025_1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application){
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    init {
        val dao = WordDatabase.getDatabase(application).wordRoom()
        repository = WordRepository(dao)
        allWords = repository.allWords
    }

    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
    fun update(word: Word) = viewModelScope.launch {
        repository.update(word)
    }
    fun delete(word: Word) = viewModelScope.launch {
        repository.delete(word)
    }
}