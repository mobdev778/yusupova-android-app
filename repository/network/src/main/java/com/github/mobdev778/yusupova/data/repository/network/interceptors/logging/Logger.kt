package com.github.mobdev778.yusupova.data.repository.network.interceptors.logging

import okhttp3.internal.platform.Platform

internal fun interface Logger {

    fun log(message: String)

    companion object {

        /** A [Logger] defaults output appropriate for the current platform. */
        @JvmField
        val DEFAULT: Logger = DefaultLogger()

        private class DefaultLogger : Logger {
            override fun log(message: String) {
                Platform.get().log(message)
            }
        }
    }
}
