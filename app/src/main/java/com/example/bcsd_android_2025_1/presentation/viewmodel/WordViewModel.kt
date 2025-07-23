
package com.example.bcsd_android_2025_1.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bcsd_android_2025_1.data.model.Word
import com.example.bcsd_android_2025_1.domain.usecase.DeleteWord
import com.example.bcsd_android_2025_1.domain.usecase.InsertWord
import com.example.bcsd_android_2025_1.domain.usecase.UpdateWord
import kotlinx.coroutines.launch

class WordViewModel(
    private val insertWord: InsertWord,
    private val updateWord: UpdateWord,
    private val deleteWord: DeleteWord,
    val allWords: LiveData<List<Word>>
) : ViewModel() {

    fun insert(word: Word) {
        viewModelScope.launch {
            insertWord(word)
        }
    }

    fun update(word: Word) {
        viewModelScope.launch {
            updateWord(word)
        }
    }

    fun delete(word: Word) {
        viewModelScope.launch {
            deleteWord(word)
        }
    }
}

class WordViewModelFactory(
    private val insertWord: InsertWord,
    private val updateWord: UpdateWord,
    private val deleteWord: DeleteWord,
    private val allWords: LiveData<List<Word>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            return WordViewModel(insertWord, updateWord, deleteWord, allWords) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}