package com.example.bcsd_android_2025_1

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch


class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WordRepository
    val allWords: LiveData<List<WordListData>>
    private val _topWord = MutableLiveData<WordListData?>()
    val topWord: LiveData<WordListData?> get() = _topWord

    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    fun insert(word:WordListData) = viewModelScope.launch { repository.insert(word)}
    fun update(word:WordListData) = viewModelScope.launch { repository.update(word)}
    fun delete(word:WordListData) = viewModelScope.launch { repository.delete(word)}
    fun setTopWord(word: WordListData) {_topWord.value = word}
}