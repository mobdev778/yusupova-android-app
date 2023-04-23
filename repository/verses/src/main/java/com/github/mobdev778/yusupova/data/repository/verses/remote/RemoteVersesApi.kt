package com.github.mobdev778.yusupova.data.repository.verses.remote

import com.github.mobdev778.yusupova.data.repository.verses.remote.model.RemoteBookList
import retrofit2.http.GET
import retrofit2.http.Path

internal interface RemoteVersesApi {

    @GET("verses_{apiLocale}.json")
    suspend fun verses(@Path("apiLocale") apiLocale: String): RemoteBookList
}
