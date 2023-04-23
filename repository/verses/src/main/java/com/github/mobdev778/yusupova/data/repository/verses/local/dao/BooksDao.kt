package com.github.mobdev778.yusupova.data.repository.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalBook
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalBookItem

@Dao
internal interface BooksDao {

    @Query("SELECT * FROM LocalBookItem")
    suspend fun getBooks(): List<LocalBookItem>

    @Query("SELECT * FROM LocalBookItem WHERE id = :id")
    suspend fun getBook(id: Int): LocalBookItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: LocalBook): Long
}
