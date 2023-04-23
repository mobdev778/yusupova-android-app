package com.github.mobdev778.yusupova.data.repository.network.interceptors.dummy

import okhttp3.Interceptor
import okhttp3.Response

object DummyInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
