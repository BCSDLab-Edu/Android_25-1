package com.example.bcsd_android_2025_1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bcsd_android_2025_1.domain.Word

@Database(entities = [Word::class], version = 2)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getDatabase(context: Context): WordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}