package com.github.mobdev778.yusupova.testing.network

import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.BindException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.SocketException

class CustomMockWebServer {

    private var mockServer: MockWebServer = MockWebServer()
    private val mocks = mutableListOf<ResponseMock>()

    fun start() {
        mockServer = startServer()
        initDispatcher()
    }

    fun url(path: String): HttpUrl {
        return mockServer.url(path)
    }

    fun addMock(mock: ResponseMock) {
        mocks.add(mock)
    }

    fun addMock(mock: (RecordedRequest) -> MockResponse?) {
        mocks.add(ResponseMockImpl(mock))
    }

    fun shutdown() {
        stopServer(mockServer)
    }

    private fun initDispatcher() {
        mockServer.dispatcher = object: Dispatcher() {

            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                mocks.forEach { mock ->
                    val mockResponse = mock.invoke(request)
                    if (mockResponse != null) {
                        return mockResponse
                    }
                }
                // 500 Internal Error
                return MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                    .setBody("{ \"code\": \"Internal Error\" }")
            }
        }
    }

    private class ResponseMockImpl(private val mock: (RecordedRequest) -> MockResponse?) : ResponseMock {
        override fun invoke(recordedRequest: RecordedRequest): MockResponse? {
            return mock.invoke(recordedRequest)
        }
    }

    companion object {

        private const val startPort = 8080
        private const val maxPort = 65535
        private val occupiedPorts = mutableSetOf<Int>()

        @Suppress("SwallowedException", "UseCheckOrError")
        @Synchronized
        private fun startServer(): MockWebServer {
            (startPort..maxPort).forEach { port ->
                if (!occupiedPorts.contains(port)) {
                    occupiedPorts.add(port)
                    val address = InetSocketAddress("localhost", port)
                    try {
                        val server = MockWebServer()
                        server.start(address.address, address.port)
                        return server
                    } catch (e: SocketException) {
                        e.printStackTrace()
                        // Port is already in use, so let's find another one
                    } catch (e: BindException) {
                        // Port is already in use, so let's find another one
                    }
                }
            }
            throw IllegalStateException("There are no available TCP ports left!")
        }

        @Synchronized
        private fun stopServer(server: MockWebServer) {
            server.shutdown()
            occupiedPorts.remove(server.port)
        }
    }
}
