package com.github.mobdev778.yusupova.data.repository.network.interceptors.logging

import timber.log.Timber

internal class TimberLogger: Logger {

    override fun log(message: String) {
        Timber.d("Http", message)
    }
}
