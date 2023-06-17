package com.github.mobdev778.yusupova.di.module

import android.content.Context
import androidx.room.Room
import com.github.mobdev778.yusupova.data.repository.verses.local.VersesLocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.local.VersesLocalDataSourceImpl
import com.github.mobdev778.yusupova.data.repository.verses.local.database.RoomVerseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object TestVersesLocalDataSourceModule {

    @Singleton
    @Provides
    internal fun provideLocalDatabase(
        context: Context,
    ): RoomVerseDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            RoomVerseDatabase::class.java
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
