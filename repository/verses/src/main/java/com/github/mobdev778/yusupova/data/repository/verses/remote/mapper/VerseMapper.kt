package com.github.mobdev778.yusupova.data.repository.verses.remote.mapper

import com.github.mobdev778.yusupova.data.repository.verses.remote.model.RemoteVerse
import com.github.mobdev778.yusupova.domain.model.verses.Verse

internal class VerseMapper {

    fun convert(verse: RemoteVerse): Verse {
        return Verse(verse.id, verse.title, verse.lines)
    }
}
