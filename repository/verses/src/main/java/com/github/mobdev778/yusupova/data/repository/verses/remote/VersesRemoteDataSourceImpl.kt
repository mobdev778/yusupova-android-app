package com.github.mobdev778.yusupova.data.repository.verses.remote

import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.verses.remote.mapper.BookMapper
import com.github.mobdev778.yusupova.domain.model.verses.Book
import retrofit2.Retrofit
import javax.inject.Inject

internal class VersesRemoteDataSourceImpl @Inject internal constructor(
    private val appConfigRepository: AppConfigRepository,
    private val retrofit: Retrofit
) : VersesRemoteDataSource {

    private val serviceApi by lazy { retrofit.create(RemoteVersesApi::class.java) }
    private val bookMapper by lazy { BookMapper() }

    override suspend fun loadBooks(): List<Book> {
        val appLocale = appConfigRepository.appLocale
        val apiType = appLocale.name.lowercase()

        val remoteBooks = serviceApi.verses(apiType)
        return remoteBooks.books.map { book -> bookMapper.convert(book) }
    }
}
