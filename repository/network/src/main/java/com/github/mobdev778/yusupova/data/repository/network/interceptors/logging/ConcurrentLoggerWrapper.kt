package com.github.mobdev778.yusupova.data.repository.network.interceptors.logging

internal class ConcurrentLoggerWrapper(
    private val requestId: Int,
    private val lock: Any,
    private val logger: Logger
): Logger {

    private val messages = mutableListOf<String>()

    override fun log(message: String) {
        messages.add(message)
    }

    fun flush() {
        synchronized(lock) {
            messages.forEach { logger.log("[$requestId] $it") }
            messages.clear()
        }
    }
}
