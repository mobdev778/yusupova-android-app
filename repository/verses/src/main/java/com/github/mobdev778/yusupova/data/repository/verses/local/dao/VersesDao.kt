package com.github.mobdev778.yusupova.data.repository.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalVerse

@Dao
internal interface VersesDao {

    @Query("SELECT * from verses WHERE book_id = :bookId")
    suspend fun getVerses(bookId: Int): List<LocalVerse>

    @Query("SELECT * from verses WHERE id = :id")
    suspend fun getVerse(id: Int): LocalVerse?

    @Query("SELECT id from verses")
    suspend fun getVerseIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verse: LocalVerse): Long
}
