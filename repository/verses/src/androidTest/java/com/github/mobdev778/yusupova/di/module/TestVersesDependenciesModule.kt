package com.github.mobdev778.yusupova.di.module

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepositoryImpl
import com.github.mobdev778.yusupova.data.repository.appconfig.MockServerUrlAnnotation
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal object TestVersesDependenciesModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppConfigRepository(@MockServerUrlAnnotation serverUrl: String): AppConfigRepository {
        return AppConfigRepositoryImpl(serverUrl)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideLogger(): HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger { message -> println(message) }

    @JvmStatic
    @Singleton
    @Provides
    fun providesOkHttp(
        logger: HttpLoggingInterceptor.Logger
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(logger).setLevel(level = HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @JvmStatic
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

    private const val CONNECT_TIMEOUT_SECONDS = 10L
    private const val READ_TIMEOUT_SECONDS = 10L
    private const val WRITE_TIMEOUT_SECONDS = 10L
}
