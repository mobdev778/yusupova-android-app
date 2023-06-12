package com.github.mobdev778.yusupova.di.module

import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.verses.remote.RemoteDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object RemoteDataSourceModule {

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(
        appConfigRepository: AppConfigRepository,
        retrofit: Retrofit
    ): RemoteDataSource {
        return RemoteDataSourceImpl(appConfigRepository, retrofit)
    }
}
