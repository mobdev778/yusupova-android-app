package com.github.mobdev778.yusupova.data.repository.verses.local.mapper

import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomBookItem
import com.github.mobdev778.yusupova.domain.model.verses.Book

internal class RoomBookMapper {

    fun convert(book: RoomBookItem): Book {
        return Book(book.id, book.title, book.verseCount, emptyList())
    }
}
