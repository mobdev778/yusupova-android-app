package com.github.mobdev778.yusupova.di.module

import com.github.mobdev778.yusupova.data.repository.verses.VersesRepository
import com.github.mobdev778.yusupova.data.repository.verses.VersesRepositoryImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.LocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object VersesModule {

    @Singleton
    @Provides
    internal fun provideVersesRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): VersesRepository {
        return VersesRepositoryImpl(localDataSource, remoteDataSource)
    }
}
