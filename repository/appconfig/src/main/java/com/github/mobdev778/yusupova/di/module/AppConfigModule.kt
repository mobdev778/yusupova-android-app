package com.github.mobdev778.yusupova.di.module

import android.content.Context
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppConfigModule {

    @Singleton
    @Provides
    fun providesAppConfigRepository(context: Context): AppConfigRepository {
        return AppConfigRepositoryImpl(context)
    }
}
