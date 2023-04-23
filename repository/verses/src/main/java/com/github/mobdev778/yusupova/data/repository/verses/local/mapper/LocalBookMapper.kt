package com.github.mobdev778.yusupova.data.repository.verses.local.mapper

import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalBookItem
import com.github.mobdev778.yusupova.domain.model.verses.Book

internal class LocalBookMapper {

    fun convert(book: LocalBookItem): Book {
        return Book(book.id, book.title, book.verseCount, emptyList())
    }
}
