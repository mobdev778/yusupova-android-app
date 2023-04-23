package com.github.mobdev778.yusupova.data.repository.network.interceptors.logging

import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.util.concurrent.atomic.AtomicInteger

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [OkHttpClient.networkInterceptors].
 *
 * The format of the logs created by this class should not be considered stable and may
 * change slightly between releases. If you need a stable logging format, use your own interceptor.
 */
internal class ConcurrentHttpLoggingInterceptor @JvmOverloads constructor(
    private val logger: Logger = Logger.DEFAULT
) : Interceptor {

    @Volatile private var headersToRedact = emptySet<String>()

    private val lock = Any()
    private val counter = AtomicInteger(0)

    @set:JvmName("level")
    @Volatile var level = Level.NONE

    @JvmName("-deprecated_level")
    @Deprecated(
        message = "moved to var",
        replaceWith = ReplaceWith(expression = "level"),
        level = DeprecationLevel.ERROR
    )
    fun getLevel(): Level = level

    @Suppress("TooGenericExceptionCaught")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val concurrentLogger = ConcurrentLoggerWrapper(counter.incrementAndGet(), lock, logger)

        concurrentLogger.logRequest(chain, request, logHeaders, logBody)

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            concurrentLogger.log("<-- HTTP FAILED: $e")
            concurrentLogger.flush()
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        concurrentLogger.logResponse(response, logHeaders, logBody, tookMs)

        return response
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    private fun ConcurrentLoggerWrapper.logRequest(
        chain: Interceptor.Chain,
        request: Request,
        logHeaders: Boolean,
        logBody: Boolean
    ) {
        val requestBody = request.body
        val method = request.method
        val connection = chain.connection()

        var requestStartMessage =
            ("--> $method ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        if (!logHeaders && requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }

        log(requestStartMessage)

        if (logHeaders) {
            val headers = request.headers

            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. When not
                // already present, force them to be included (if available) so their values are known.
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        log("Content-Type: $it")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        log("Content-Length: ${requestBody.contentLength()}")
                    }
                }
            }

            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (!logBody || requestBody == null) {
                log("--> END $method")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                log("--> END $method (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                log("--> END $method (duplex request body omitted)")
            } else if (requestBody.isOneShot()) {
                log("--> END $method (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8

                log("")
                if (buffer.isProbablyUtf8()) {
                    log(buffer.readString(charset))
                    log("--> END $method (${requestBody.contentLength()}-byte body)")
                } else {
                    log("--> END $method (binary ${requestBody.contentLength()}-byte body omitted)")
                }
            }
        }
        flush()
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    private fun ConcurrentLoggerWrapper.logResponse(
        response: Response,
        logHeaders: Boolean,
        logBody: Boolean,
        tookMs: Long
    ) {

        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        log(
            "<-- ${response.code}" +
            "${if (response.message.isEmpty()) "" else ' ' + response.message} " +
            "${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})"
        )

        if (logHeaders) {
            val headers = response.headers
            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (!logBody || !response.promisesBody()) {
                log("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8

                if (!buffer.isProbablyUtf8()) {
                    log("")
                    log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    flush()
                    return
                }

                if (contentLength != 0L) {
                    log("")
                    log(buffer.clone().readString(charset))
                }

                if (gzippedLength != null) {
                    log("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    log("<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        }

        flush()
    }

    @Suppress("ReturnCount")
    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val buffer = Buffer()
            val byteCount = size.coerceAtMost(BUFFER_SIZE)
            copyTo(buffer, 0, byteCount)
            for (i in 0 until BUFFER_CHARS) {
                if (buffer.exhausted()) {
                    break
                }
                val codePoint = buffer.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun ConcurrentLoggerWrapper.logHeader(headers: Headers, i: Int) {
        val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
        log(headers.name(i) + ": " + value)
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    companion object {
        private const val BUFFER_CHARS = 16
        private const val BUFFER_SIZE = (BUFFER_CHARS * 4).toLong()
    }
}
