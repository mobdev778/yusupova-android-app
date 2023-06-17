package com.github.mobdev778.yusupova.data.repository.verses

import com.github.mobdev778.yusupova.data.repository.BaseRepositoryTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class VersesRepositoryTest : BaseRepositoryTest() {

    private val repository by lazy { component.versesRepository }

    @Test
    fun testSyncAndGetBooks() {
        addMock("verses_en.json", BaseRepositoryTest::class.java, "verses_en.json")
        val books = runBlocking {
            repository.sync().await()
            repository.getBooks()
        }
        MatcherAssert.assertThat(books.isNotEmpty(), Is.`is`(true))
    }

    @ParameterizedTest
    @MethodSource("booksDataProvider")
    fun testSyncAndGetBook(id: Int, expectedTitle: String) {
        addMock("verses_en.json", BaseRepositoryTest::class.java, "verses_en.json")
        val book = runBlocking {
            repository.sync().await()
            repository.getBook(id)
        }
        MatcherAssert.assertThat(book?.title, Is.`is`(expectedTitle))
    }

    @ParameterizedTest
    @MethodSource("versesDataProvider")
    fun testSyncAndGetVerse(id: Int, expectedTitle: String) {
        addMock("verses_en.json", BaseRepositoryTest::class.java, "verses_en.json")
        val verse = runBlocking {
            repository.sync().await()
            repository.getVerse(id)
        }
        MatcherAssert.assertThat(verse?.title, Is.`is`(expectedTitle))
    }

    companion object {

        @JvmStatic
        fun booksDataProvider() = listOf(
            Arguments.of(1, "Agniya Barto"),
            Arguments.of(2, "Marshak")
        )

        @JvmStatic
        fun versesDataProvider() = listOf(
            Arguments.of(1, "Myachik"),
            Arguments.of(2, "Bychok"),
            Arguments.of(3, "Bagazh"),
            Arguments.of(4, "Gde obedal vorobey"),
            Arguments.of(5, "Horoshiy den")
        )
    }
}
