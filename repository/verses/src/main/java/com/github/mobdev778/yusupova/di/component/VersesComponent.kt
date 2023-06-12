package com.github.mobdev778.yusupova.di.component

import com.github.mobdev778.yusupova.di.module.VersesLocalDataSourceModule
import com.github.mobdev778.yusupova.di.module.VersesRemoteDataSourceModule
import com.github.mobdev778.yusupova.di.module.VersesModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        VersesLocalDataSourceModule::class,
        VersesRemoteDataSourceModule::class,
        VersesModule::class
    ],
    dependencies = [
        VersesDependencies::class
    ]
)
interface VersesComponent {

    @Component.Factory
    interface Factory {

        fun create(dependencies: VersesDependencies): VersesComponent
    }
}
