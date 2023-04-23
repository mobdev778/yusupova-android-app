package com.github.mobdev778.yusupova.data.repository.verses

import com.github.mobdev778.yusupova.data.repository.verses.local.LocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.RemoteDataSource
import com.github.mobdev778.yusupova.domain.model.verses.Book
import com.github.mobdev778.yusupova.domain.model.verses.Verse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

internal class VersesRepositoryImpl @Inject internal constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
): VersesRepository {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun sync(): kotlinx.coroutines.Deferred<Unit> {
        return coroutineScope.async {
            val books = remoteDataSource.loadBooks()
            localDataSource.writeBooks(books)
        }
    }

    override suspend fun getBooks(): List<Book> {
        return localDataSource.getBooks()
    }

    override suspend fun getBook(bookId: Int): Book? {
        return localDataSource.getBook(bookId)
    }

    override suspend fun getVerses(bookId: Int): List<Verse> {
        return localDataSource.getVerses(bookId)
    }

    override suspend fun getVerse(verseId: Int): Verse? {
        return localDataSource.getVerse(verseId)
    }

    override suspend fun getVerseIds(): List<Int> {
        return localDataSource.getVerseIds()
    }
}
