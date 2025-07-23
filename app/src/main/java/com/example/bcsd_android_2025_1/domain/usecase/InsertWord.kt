package com.example.bcsd_android_2025_1.domain.usecase

import com.example.bcsd_android_2025_1.data.model.Word
import com.example.bcsd_android_2025_1.data.repository.WordRepository

class InsertWord(private val repository: WordRepository) {
    suspend operator fun invoke(word: Word) {
        repository.insert(word)
    }
}