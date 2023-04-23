package com.github.mobdev778.yusupova.data.repository.verses.remote.mapper

import com.github.mobdev778.yusupova.data.repository.verses.remote.model.RemoteBook
import com.github.mobdev778.yusupova.domain.model.verses.Book

internal class BookMapper {

    private val verseMapper by lazy { VerseMapper() }

    fun convert(book: RemoteBook): Book {
        val verses = book.verses.map { verse -> verseMapper.convert(verse) }
        return Book(book.id, book.name, verses.size, verses)
    }
}
