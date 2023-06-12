package com.github.mobdev778.yusupova.di.component

import android.content.Context
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository

interface NetworkDependencies {

    fun appContext(): Context

    fun appConfigRepository(): AppConfigRepository
}
