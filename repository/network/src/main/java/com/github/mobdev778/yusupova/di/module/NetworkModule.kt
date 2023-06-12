package com.github.mobdev778.yusupova.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.network.annotation.ChuckerInterceptorAnnotation
import com.github.mobdev778.yusupova.data.repository.network.annotation.LoggingInterceptorAnnotation
import com.github.mobdev778.yusupova.data.repository.network.interceptors.dummy.DummyInterceptor
import com.github.mobdev778.yusupova.data.repository.network.interceptors.logging.ConcurrentHttpLoggingInterceptor
import com.github.mobdev778.yusupova.data.repository.network.interceptors.logging.Level
import com.github.mobdev778.yusupova.data.repository.network.interceptors.logging.TimberLogger
import com.github.mobdev778.yusupova.repository.network.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @LoggingInterceptorAnnotation
    @Provides
    fun providesLoggingInterceptor(): Interceptor = when {
        BuildConfig.DEBUG -> ConcurrentHttpLoggingInterceptor(TimberLogger()).apply { level = Level.BODY }
        else -> DummyInterceptor
    }

    @Singleton
    @ChuckerInterceptorAnnotation
    @Provides
    fun providesChuckerInterceptor(context: Context): Interceptor = when {
        BuildConfig.DEBUG -> ChuckerInterceptor.Builder(context.applicationContext).build()
        else -> DummyInterceptor
    }

    @Singleton
    @Provides
    fun providesOkHttp(
        @LoggingInterceptorAnnotation loggingInterceptor: Interceptor,
        @ChuckerInterceptorAnnotation chuckerInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            addOptionalInterceptor(loggingInterceptor)
            addOptionalInterceptor(chuckerInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        appConfigRepository: AppConfigRepository,
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val serverUrl = appConfigRepository.serverUrl
        val converterFactory = MoshiConverterFactory.create(moshi)
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(serverUrl)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun OkHttpClient.Builder.addOptionalInterceptor(interceptor: Interceptor) = apply {
        if (interceptor !is DummyInterceptor) {
            addInterceptor(interceptor)
        }
    }

    companion object {
        const val CONNECT_TIMEOUT_SECONDS = 10L
        const val READ_TIMEOUT_SECONDS = 10L
        const val WRITE_TIMEOUT_SECONDS = 10L
    }
}
