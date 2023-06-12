package com.github.mobdev778.yusupova.data.repository.verses.local

import com.github.mobdev778.yusupova.data.repository.verses.local.database.RoomVerseDatabase
import com.github.mobdev778.yusupova.data.repository.verses.local.mapper.RoomBookMapper
import com.github.mobdev778.yusupova.data.repository.verses.local.mapper.RoomVerseMapper
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomBook
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerse
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerseLine
import com.github.mobdev778.yusupova.domain.model.verses.Book
import com.github.mobdev778.yusupova.domain.model.verses.Verse
import java.util.concurrent.atomic.AtomicInteger

internal class LocalDataSourceImpl(
    private val database: RoomVerseDatabase
) : LocalDataSource {

    private val booksDao by lazy { database.booksDao() }
    private val versesDao by lazy { database.versesDao() }
    private val verseLinesDao by lazy { database.verseLinesDao() }

    private val bookMapper by lazy { RoomBookMapper() }
    private val verseMapper by lazy { RoomVerseMapper() }

    override suspend fun getBooks(): List<Book> {
        val localBooks = booksDao.getBooks()
        return localBooks.map {
            bookMapper.convert(it)
        }
    }

    override suspend fun getBook(bookId: Int): Book? {
        return booksDao.getBook(bookId)?.let {
            bookMapper.convert(it)
        }
    }

    override suspend fun getVerses(bookId: Int): List<Verse> {
        return versesDao.getVerses(bookId).map {
            verseMapper.convert(it)
        }
    }

    override suspend fun getVerse(verseId: Int): Verse? {
        return versesDao.getVerse(verseId)?.let {
            val lines = verseLinesDao.getLines(it.id)
            verseMapper.convert(it, lines)
        }
    }

    override suspend fun getVerseIds(): List<Int> {
        return versesDao.getVerseIds()
    }

    override suspend fun writeBooks(books: List<Book>) = with (database.openHelper.writableDatabase) {
        beginTransaction()

        val lineId = AtomicInteger(0)

        books.forEach { book ->
            val localBook = RoomBook(book.id, book.title)
            booksDao.insert(localBook)
            book.verses.forEach { verse ->
                insertVerse(lineId, book, verse)
            }
        }
        setTransactionSuccessful()
        endTransaction()
    }

    override suspend fun clear() {
        versesDao.deleteAll()
    }

    private suspend fun insertVerse(lineId: AtomicInteger, book: Book, verse: Verse) {
        val localVerse = RoomVerse(verse.id, book.id, verse.title)
        versesDao.insert(localVerse)

        verse.lines.forEach {
            val localLine = RoomVerseLine(lineId.incrementAndGet(), verse.id, it)
            verseLinesDao.insert(localLine)
        }
    }
}
