package com.github.mobdev778.yusupova.data.repository.verses.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verse_lines",
    indices = [
        Index("verse_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = RoomVerse::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("verse_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class RoomVerseLine(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "verse_id")
    val verseId: Int,

    @ColumnInfo(name = "line")
    val line: String
)
