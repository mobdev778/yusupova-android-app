package com.github.mobdev778.yusupova.data.repository.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerse

@Dao
internal interface VersesDao {

    @Query("SELECT * FROM verses WHERE book_id = :bookId")
    suspend fun getVerses(bookId: Int): List<RoomVerse>

    @Query("SELECT * FROM verses WHERE id = :id")
    suspend fun getVerse(id: Int): RoomVerse?

    @Query("SELECT id FROM verses")
    suspend fun getVerseIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verse: RoomVerse): Long

    @Query("DELETE FROM verses")
    suspend fun deleteAll()
}
