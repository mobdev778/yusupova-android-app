package com.github.mobdev778.yusupova.data.repository.verses.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
internal data class RoomBook(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String
)
