package com.github.mobdev778.yusupova.data.repository.verses.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    indices = [
        Index("book_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocalBook::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("book_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class LocalVerse(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "book_id")
    val bookId: Int,

    @ColumnInfo(name = "title")
    val title: String
)
