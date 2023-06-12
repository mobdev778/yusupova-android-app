package com.github.mobdev778.yusupova.data.repository.verses.local

import com.github.mobdev778.yusupova.domain.model.verses.Book
import com.github.mobdev778.yusupova.domain.model.verses.Verse

interface VersesLocalDataSource {

    suspend fun getBooks(): List<Book>

    suspend fun getBook(bookId: Int): Book?

    suspend fun getVerses(bookId: Int): List<Verse>

    suspend fun getVerse(verseId: Int): Verse?

    suspend fun getVerseIds(): List<Int>

    suspend fun writeBooks(books: List<Book>)

    suspend fun clear()
}
