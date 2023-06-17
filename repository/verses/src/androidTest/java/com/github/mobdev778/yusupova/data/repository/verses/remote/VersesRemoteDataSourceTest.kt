package com.github.mobdev778.yusupova.data.repository.verses.remote

import com.github.mobdev778.yusupova.data.repository.BaseRepositoryTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

internal class VersesRemoteDataSourceTest : BaseRepositoryTest() {

    private val dataSource by lazy { component.remoteDataSource }

    @Test
    fun testLoadBooks() {
        addMock("verses_en.json", BaseRepositoryTest::class.java, "verses_en.json")
        val books = runBlocking {
            dataSource.loadBooks()
        }
        assertThat(books.isNotEmpty(), `is`(true))
    }
}
