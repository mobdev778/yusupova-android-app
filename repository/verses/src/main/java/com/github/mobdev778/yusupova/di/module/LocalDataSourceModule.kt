package com.github.mobdev778.yusupova.di.module

import android.content.Context
import androidx.room.Room
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.verses.local.LocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.local.LocalDataSourceImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.database.RoomVerseDatabase
import com.github.mobdev778.yusupova.di.annotation.DatabaseNameAnnotation
import com.github.mobdev778.yusupova.repository.verses.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalDataSourceModule {

    @Singleton
    @Provides
    @DatabaseNameAnnotation
    internal fun provideDatabaseName(appConfigRepository: AppConfigRepository): String {
        if (BuildConfig.DEBUG) {
            return "debug_verses_${appConfigRepository.appLocale}.db"
        }
        return "verses_${appConfigRepository.appLocale}.db"
    }

    @Singleton
    @Provides
    internal fun provideLocalDatabase(
        context: Context,
        @DatabaseNameAnnotation databaseName: String
    ): RoomVerseDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RoomVerseDatabase::class.java,
            databaseName
        ).build()
    }

    @Singleton
    @Provides
    internal fun provideLocalDataSource(
        abstractVerseDatabase: RoomVerseDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(abstractVerseDatabase)
    }
}
