package com.github.mobdev778.yusupova.di.component

import com.github.mobdev778.yusupova.di.module.NetworkModule
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ],
    dependencies = [
        NetworkDependencies::class
    ]
)
interface NetworkComponent {

    fun retrofit(): Retrofit

    @Component.Factory
    interface Factory {

        fun create(dependencies: NetworkDependencies): NetworkComponent
    }
}
