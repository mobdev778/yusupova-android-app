package com.github.mobdev778.yusupova.data.repository.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerseLine

@Dao
internal interface VerseLinesDao {

    @Query("SELECT line FROM verse_lines WHERE verse_id = :id")
    suspend fun getLines(id: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verse: RoomVerseLine): Long
}
