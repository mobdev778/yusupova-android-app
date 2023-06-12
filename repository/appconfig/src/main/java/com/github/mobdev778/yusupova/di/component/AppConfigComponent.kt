package com.github.mobdev778.yusupova.di.component

import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.di.module.AppConfigModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppConfigModule::class
    ],
    dependencies = [
        AppConfigDependencies::class
    ],
)
interface AppConfigComponent {

    fun getAppConfigRepository(): AppConfigRepository

    @Component.Factory
    interface Factory {

        fun create(dependencies: AppConfigDependencies): AppConfigComponent
    }
}
