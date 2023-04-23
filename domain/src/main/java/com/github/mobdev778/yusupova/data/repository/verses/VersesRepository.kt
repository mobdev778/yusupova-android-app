package com.github.mobdev778.yusupova.data.repository.verses

import com.github.mobdev778.yusupova.domain.model.verses.Book
import com.github.mobdev778.yusupova.domain.model.verses.Verse
import kotlinx.coroutines.Deferred

interface VersesRepository {

    suspend fun sync(): Deferred<Boolean>

    suspend fun getBooks(): List<Book>

    suspend fun getVerses(bookId: Int): List<Verse>

    suspend fun getVerse(verseId: Int): Verse

    suspend fun getVerseIds(): List<Int>
}
