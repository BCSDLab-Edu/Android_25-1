package com.example.bcsd_android_2025_1

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Query("SELECT * FROM word_table ORDER BY id DESC")
    fun getAllwords(): LiveData<List<WordListData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: WordListData)

    @Update
    suspend fun update(word: WordListData)

    @Delete
    suspend fun delete(wordListData: WordListData)

}