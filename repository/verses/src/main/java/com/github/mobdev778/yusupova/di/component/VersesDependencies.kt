package com.github.mobdev778.yusupova.di.component

import android.content.Context
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import retrofit2.Retrofit

interface VersesDependencies {

    fun appContext(): Context

    fun appConfigRepository(): AppConfigRepository

    fun retrofit(): Retrofit
}
