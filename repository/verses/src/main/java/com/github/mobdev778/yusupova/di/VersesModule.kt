package com.github.mobdev778.yusupova.di

import android.content.Context
import androidx.room.Room
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.verses.VersesRepository
import com.github.mobdev778.yusupova.data.repository.verses.VersesRepositoryImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.LocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.local.LocalDataSourceImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.database.AbstractVerseDatabase
import com.github.mobdev778.yusupova.data.repository.verses.remote.RemoteDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object VersesModule {

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(
        appConfigRepository: AppConfigRepository,
        retrofit: Retrofit
    ): RemoteDataSource {
        return RemoteDataSourceImpl(appConfigRepository, retrofit)
    }

    @Singleton
    @Provides
    internal fun provideLocalDatabase(
        context: Context,
        appConfigRepository: AppConfigRepository
    ): AbstractVerseDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AbstractVerseDatabase::class.java,
            "verses_${appConfigRepository.appLocale}.db"
        ).build()
    }

    @Singleton
    @Provides
    internal fun provideLocalDataSource(
        abstractVerseDatabase: AbstractVerseDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(abstractVerseDatabase)
    }

    @Singleton
    @Provides
    internal fun provideVersesRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): VersesRepository {
        return VersesRepositoryImpl(localDataSource, remoteDataSource)
    }
}
