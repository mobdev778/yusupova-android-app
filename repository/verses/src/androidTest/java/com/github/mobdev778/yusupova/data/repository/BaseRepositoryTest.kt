package com.github.mobdev778.yusupova.data.repository

import com.github.mobdev778.yusupova.di.component.DaggerTestVersesComponent
import com.github.mobdev778.yusupova.di.component.TestVersesComponent
import com.github.mobdev778.yusupova.testing.network.CustomMockWebServer
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.nio.charset.StandardCharsets

internal open class BaseRepositoryTest {

    fun <T : BaseRepositoryTest> addMock(path: String, clazz: Class<T>, fileName: String) {
        mockWebServer.addMock { request ->
            when {
                request.path?.contains(path) == true -> {
                    val body = clazz.loadFile(fileName)
                    MockResponse().setResponseCode(200).setBody(body)
                }
                else -> null
            }
        }
    }

    private fun <T : BaseRepositoryTest> Class<T>.loadFile(fileName: String): String {
        val stream = getResourceAsStream(fileName)
        return when {
            stream != null -> {
                String(stream.readBytes(), StandardCharsets.UTF_8).trim()
            }
            else -> {
                val path = name.replace(".", "/")
                    .replace(simpleName, "")
                    .plus(fileName)
                throw IllegalArgumentException("File: /test/resources/$path not found")
            }
        }
    }

    companion object {

        lateinit var mockWebServer: CustomMockWebServer
        lateinit var component: TestVersesComponent

        @JvmStatic
        @BeforeAll
        fun initAll() {
            mockWebServer = CustomMockWebServer()
            mockWebServer.start()

            val url = mockWebServer.url("/").toString()
            component = DaggerTestVersesComponent.factory().create(url)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            mockWebServer.shutdown()
        }
    }
}
