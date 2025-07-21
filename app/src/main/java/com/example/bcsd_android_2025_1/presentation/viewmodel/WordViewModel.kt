
package com.example.bcsd_android_2025_1.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bcsd_android_2025_1.data.model.Word
import com.example.bcsd_android_2025_1.data.repository.WordRepository
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    val allWords: LiveData<List<Word>> = repository.getAllWords()

    fun insert(word: Word) {
        viewModelScope.launch {
            repository.insert(word)
        }
    }

    fun update(word: Word) {
        viewModelScope.launch {
            repository.update(word)
        }
    }

    fun delete(word: Word) {
        viewModelScope.launch {
            repository.delete(word)
        }
    }
}

class WordViewModelFactory(
    private val repository: WordRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}