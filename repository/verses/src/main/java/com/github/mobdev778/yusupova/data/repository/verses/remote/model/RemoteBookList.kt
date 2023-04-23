package com.github.mobdev778.yusupova.data.repository.verses.remote.model

import com.squareup.moshi.Json

internal class RemoteBookList {

    @Json(name = "version")
    var version: Int = 0

    @Json(name = "books")
    lateinit var books: List<RemoteBook>
}
