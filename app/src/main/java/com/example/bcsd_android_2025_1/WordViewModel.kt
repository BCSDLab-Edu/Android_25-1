package com.example.bcsd_android_2025_1

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>
    private val _selectedWord = MutableLiveData<Word?>()
    val selectedWord: LiveData<Word?> get() = _selectedWord

    init {
        val dao = WordDatabase.getDatabase(application).wordDao()
        repository = WordRepository(dao)
        allWords = repository.allWords
    }
    fun insert(word: Word) = viewModelScope.launch { repository.insert(word) }
    fun update(word: Word) = viewModelScope.launch { repository.update(word) }
    fun delete(word: Word) = viewModelScope.launch { repository.delete(word) }
    fun selectWord(word: Word) {
        _selectedWord.value = word
    }
}
