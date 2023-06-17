package com.github.mobdev778.yusupova.di.component

import com.github.mobdev778.yusupova.data.repository.appconfig.MockServerUrlAnnotation
import com.github.mobdev778.yusupova.data.repository.verses.VersesRepository
import com.github.mobdev778.yusupova.data.repository.verses.local.VersesLocalDataSource
import com.github.mobdev778.yusupova.data.repository.verses.remote.VersesRemoteDataSource
import com.github.mobdev778.yusupova.di.module.TestVersesDependenciesModule
import com.github.mobdev778.yusupova.di.module.TestVersesLocalDataSourceModule
import com.github.mobdev778.yusupova.di.module.VersesModule
import com.github.mobdev778.yusupova.di.module.VersesRemoteDataSourceModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestVersesDependenciesModule::class,
        TestVersesLocalDataSourceModule::class,
        VersesRemoteDataSourceModule::class,
        VersesModule::class
    ],
)
internal interface TestVersesComponent : VersesDependencies {

    val versesRepository: VersesRepository
    val localDataSource: VersesLocalDataSource
    val remoteDataSource: VersesRemoteDataSource

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance @MockServerUrlAnnotation serverUrl: String): TestVersesComponent
    }
}
