package com.github.mobdev778.yusupova.data.repository.verses.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.mobdev778.yusupova.data.repository.verses.local.database.RoomVerseDatabase
import com.github.mobdev778.yusupova.domain.model.verses.Book
import com.github.mobdev778.yusupova.domain.model.verses.Verse
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class VersesLocalDataSourceTest {

    @BeforeEach
    fun clearDb() {
        runBlocking {
            dataSource.clear()
        }
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testGetBooks(books: List<Book>) {
        val reloadedBooks = runBlocking {
            dataSource.writeBooks(books)
            dataSource.getBooks()
        }
        assertThat(books.size, `is`(reloadedBooks.size))
        books.indices.forEach { index ->
            assertThat(reloadedBooks[index].title, `is`(books[index].title))
            assertThat(reloadedBooks[index].verseCount, `is`(books[index].verseCount))
        }
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testGetBook(books: List<Book>) {
        val originalBook = books[0]
        val reloadedBook = runBlocking {
            dataSource.writeBooks(books)
            dataSource.getBook(1)
        }

        assertThat(reloadedBook?.title, `is`(originalBook.title))
        assertThat(reloadedBook?.verseCount, `is`(originalBook.verseCount))
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testGetVerses(books: List<Book>) {
        val reloadedVerses = runBlocking {
            dataSource.writeBooks(books)
            dataSource.getVerses(1)
        }
        val originalVerses = books[0].verses

        assertThat(reloadedVerses.size, `is`(originalVerses.size))
        reloadedVerses.indices.forEach { index ->
            assertThat(reloadedVerses[index].id, `is`(originalVerses[index].id))
            assertThat(reloadedVerses[index].title, `is`(originalVerses[index].title))
        }
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testGetVerse(books: List<Book>) {
        val idVerseMap = books
            .flatMap { it.verses }
            .associateBy { it.id }
        assertThat(idVerseMap.isNotEmpty(), `is`(true))

        runBlocking {
            dataSource.writeBooks(books)
        }

        idVerseMap.forEach {
            runBlocking {
                val originalVerse = it.value
                val reloadedVerse = dataSource.getVerse(it.key)
                assertThat(reloadedVerse != null, `is`(true))
                assertThat(reloadedVerse?.id, `is`(originalVerse.id))
                assertThat(reloadedVerse?.title, `is`(originalVerse.title))
            }
        }
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testGetVerseIds(books: List<Book>) {
        val originalIds = books
            .flatMap { it.verses }
            .map { it.id }
        assertThat(originalIds.isNotEmpty(), `is`(true))

        runBlocking {
            dataSource.writeBooks(books)
        }

        runBlocking {
            val reloadedIds = dataSource.getVerseIds()
            assertThat(reloadedIds.size, `is`(originalIds.size))
            assertThat(reloadedIds.toList(), `is`(originalIds.toList()))
        }
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testClear(books: List<Book>) {
        val originalIds = books
            .flatMap { it.verses }
            .map { it.id }
        assertThat(originalIds.isNotEmpty(), `is`(true))

        runBlocking {
            dataSource.writeBooks(books)
            val reloadedIds = dataSource.getVerseIds()
            assertThat(reloadedIds.size, `is`(originalIds.size))
            dataSource.clear()
            val reloadedIds2 = dataSource.getVerseIds()
            assertThat(reloadedIds2.size, `is`(0))
        }
    }

    companion object {

        private lateinit var dataSource: VersesLocalDataSource

        @JvmStatic
        @BeforeAll
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val dataBase = Room.inMemoryDatabaseBuilder(
                context,
                RoomVerseDatabase::class.java
            ).build()
            dataSource = VersesLocalDataSourceImpl(dataBase)
        }

        @JvmStatic
        fun booksDataProvider(): List<Arguments> {
            val verseA1 = createVerse(1, "Myachik", "Nasha Tanya gromko plachet:\nUronila v rechku myachik.")
            val verseA2 = createVerse(2, "Mishka", "Uronili mishku na pol,\nOtorvali mishke lapu.")
            val verseB1 = createVerse(3, "Vesyoliy stchyot", "Vot odin il yedinitsa\nOchen tonkaya, kak spitsa.")
            val verseB2 = createVerse(4, "Kto on", "V gorod pribil k nam kogda-to\nMister Flint, zamorskiy gost.")
            val verseB3 = createVerse(5, "Volk i lisa", "Seriy volk v gustom lesu\nVstretil rizhuyu lisu.")

            return listOf(
                Arguments.of(
                    listOf(
                        createBook(1, "Barto", listOf(verseA1, verseA2))
                    )
                ),
                Arguments.of(
                    listOf(
                        createBook(1, "Marshak", listOf(verseB1, verseB2, verseB3))
                    )
                ),
                Arguments.of(
                    listOf(
                        createBook(1, "Barto", listOf(verseA1, verseA2)),
                        createBook(2, "Marshak", listOf(verseB1, verseB2, verseB3))
                    )
                )
            )
        }

        private fun createVerse(id: Int, title: String, text: String): Verse {
            return Verse(id, title, text.split("\n"))
        }

        private fun createBook(id: Int, title: String, verses: List<Verse>): Book {
            return Book(id, title, verses.size, verses)
        }
    }
}
