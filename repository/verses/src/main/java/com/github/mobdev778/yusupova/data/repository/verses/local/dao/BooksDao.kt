package com.github.mobdev778.yusupova.data.repository.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomBook
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomBookItem

@Dao
internal interface BooksDao {

    @Query("SELECT * FROM RoomBookItem")
    suspend fun getBooks(): List<RoomBookItem>

    @Query("SELECT * FROM RoomBookItem WHERE id = :id")
    suspend fun getBook(id: Int): RoomBookItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: RoomBook): Long
}
