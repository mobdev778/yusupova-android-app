package com.github.mobdev778.yusupova.data.repository.verses.remote.model

import com.squareup.moshi.Json

internal class RemoteBook {

    @Json(name = "id")
    var id: Int = 0

    @Json(name = "name")
    lateinit var name: String

    @Json(name = "verses")
    lateinit var verses: List<RemoteVerse>
}
