package com.example.bcsd_android_2025_1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bcsd_android_2025_1.domain.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM word_table ORDER BY id DESC")
    fun getAllWords() : LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: Word)

    @Update
    suspend fun update(word: Word)

    @Delete
    suspend fun delete(word: Word)
}