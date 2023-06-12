package com.github.mobdev778.yusupova.data.repository.verses.local.mapper

import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerse
import com.github.mobdev778.yusupova.domain.model.verses.Verse

internal class RoomVerseMapper {

    fun convert(verse: RoomVerse): Verse {
        return Verse(verse.id, verse.title, emptyList())
    }

    fun convert(verse: RoomVerse, lines: List<String>): Verse {
        return Verse(verse.id, verse.title, lines)
    }
}
