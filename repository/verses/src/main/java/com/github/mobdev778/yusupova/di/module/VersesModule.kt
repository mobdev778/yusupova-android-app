package com.github.mobdev778.yusupova.di.module

import com.github.mobdev778.yusupova.data.repository.verses.VersesRepository
import com.github.mobdev778.yusupova.data.repository.verses.VersesRepositoryImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.VersesLocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.VersesRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object VersesModule {

    @Singleton
    @Provides
    internal fun provideVersesRepository(
        localDataSource: VersesLocalDataSource,
        remoteDataSource: VersesRemoteDataSource
    ): VersesRepository {
        return VersesRepositoryImpl(localDataSource, remoteDataSource)
    }
}
