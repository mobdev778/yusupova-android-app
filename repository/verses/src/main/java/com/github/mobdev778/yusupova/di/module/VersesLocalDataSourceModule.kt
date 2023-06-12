package com.github.mobdev778.yusupova.di.module

import android.content.Context
import androidx.room.Room
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.verses.local.VersesLocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.local.VersesLocalDataSourceImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.database.RoomVerseDatabase
import com.github.mobdev778.yusupova.di.annotation.DatabaseNameAnnotation
import com.github.mobdev778.yusupova.repository.verses.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object VersesLocalDataSourceModule {

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
    ): VersesLocalDataSource {
        return VersesLocalDataSourceImpl(abstractVerseDatabase)
    }
}
