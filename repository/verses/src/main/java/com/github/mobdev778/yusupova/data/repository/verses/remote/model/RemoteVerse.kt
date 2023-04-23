package com.github.mobdev778.yusupova.data.repository.verses.remote.model

import com.squareup.moshi.Json

internal class RemoteVerse {

    @Json(name = "id")
    var id: Int = 0

    @Json(name = "name")
    lateinit var title: String

    @Json(name = "lines")
    lateinit var lines: List<String>
}
