package com.github.mobdev778.yusupova.data.repository.verses.local.mapper

import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalVerse
import com.github.mobdev778.yusupova.domain.model.verses.Verse

internal class LocalVerseMapper {

    fun convert(verse: LocalVerse): Verse {
        return Verse(verse.id, verse.title, emptyList())
    }

    fun convert(verse: LocalVerse, lines: List<String>): Verse {
        return Verse(verse.id, verse.title, lines)
    }
}
