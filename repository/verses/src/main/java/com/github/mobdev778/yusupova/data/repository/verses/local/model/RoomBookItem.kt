package com.github.mobdev778.yusupova.data.repository.verses.local.model

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT books.id as id, books.title as title, COUNT(*) as verseCount FROM verses 
    INNER JOIN books ON verses.book_id = books.id 
    GROUP BY verses.book_id
"""
)
data class RoomBookItem(
    val id: Int,
    val title: String,
    val verseCount: Int
)
