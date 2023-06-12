package com.github.mobdev778.yusupova.data.repository.verses.remote

import com.github.mobdev778.yusupova.domain.model.verses.Book

internal interface VersesRemoteDataSource {

    suspend fun loadBooks(): List<Book>
}

