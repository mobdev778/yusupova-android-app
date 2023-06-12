package com.github.mobdev778.yusupova.di.module

import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.verses.remote.VersesRemoteDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.VersesRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object VersesRemoteDataSourceModule {

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(
        appConfigRepository: AppConfigRepository,
        retrofit: Retrofit
    ): VersesRemoteDataSource {
        return VersesRemoteDataSourceImpl(appConfigRepository, retrofit)
    }
}
