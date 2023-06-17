package com.github.mobdev778.yusupova.testing.network

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

interface ResponseMock {

    fun invoke(recordedRequest: RecordedRequest): MockResponse?

}
