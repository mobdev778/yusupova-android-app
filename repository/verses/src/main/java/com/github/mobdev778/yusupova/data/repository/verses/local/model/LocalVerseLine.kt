package com.github.mobdev778.yusupova.data.repository.verses.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.mobdev778.yusupova.domain.model.verses.Verse

@Entity(
    tableName = "verse_lines",
    indices = [
        Index("verse_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocalVerse::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("verse_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class LocalVerseLine(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "verse_id")
    val verseId: Int,

    @ColumnInfo(name = "line")
    val line: String
)
