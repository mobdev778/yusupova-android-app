package com.github.mobdev778.yusupova.app

import android.app.Application
import android.content.Context
import com.github.mobdev778.yusupova.data.repository.appconfig.AppConfigRepository
import com.github.mobdev778.yusupova.di.component.AppConfigComponent
import com.github.mobdev778.yusupova.di.component.AppConfigDependencies
import com.github.mobdev778.yusupova.di.component.ApplicationComponent
import com.github.mobdev778.yusupova.di.component.DaggerAppConfigComponent
import com.github.mobdev778.yusupova.di.component.DaggerApplicationComponent
import com.github.mobdev778.yusupova.di.component.DaggerNetworkComponent
import com.github.mobdev778.yusupova.di.component.DaggerVersesComponent
import com.github.mobdev778.yusupova.di.component.NetworkComponent
import com.github.mobdev778.yusupova.di.component.NetworkDependencies
import com.github.mobdev778.yusupova.di.component.VersesComponent
import com.github.mobdev778.yusupova.di.component.VersesDependencies
import com.github.mobdev778.yusupova.navigation.NavGraphRegistry
import com.github.mobdev778.yusupova.navigation.Screen
import com.github.mobdev778.yusupova.ui.screens.home.HomeScreen
import com.github.mobdev778.yusupova.ui.screens.splash.SplashScreen

class YusupovaApplication : Application(), AppConfigDependencies, NetworkDependencies, VersesDependencies {

    lateinit var appConfigComponent: AppConfigComponent
    lateinit var networkComponent: NetworkComponent
    lateinit var versesComponent: VersesComponent
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initNavigationGraph()
    }

    override fun appContext(): Context {
        return this
    }

    override fun appConfigRepository(): AppConfigRepository {
        return provideAppConfigComponent().getAppConfigRepository()
    }

    override fun retrofit(): retrofit2.Retrofit {
        return provideNetworkComponent().retrofit()
    }

    private fun initDagger() {
        val versesComponent = provideVersesComponent()
        applicationComponent = DaggerApplicationComponent.factory().create(this, versesComponent)
    }

    private fun initNavigationGraph() {
        NavGraphRegistry.addDefaultScreen(Screen.Splash.route) { navController -> SplashScreen(navController) }
        NavGraphRegistry.addScreen(Screen.Home.route) { navController -> HomeScreen(navController) }
    }

    private fun provideAppConfigComponent(): AppConfigComponent {
        if (!this::appConfigComponent.isInitialized) {
            appConfigComponent = DaggerAppConfigComponent.factory().create(this)
        }
        return appConfigComponent
    }

    private fun provideNetworkComponent(): NetworkComponent {
        if (!this::networkComponent.isInitialized) {
            networkComponent = DaggerNetworkComponent.factory().create(this)
        }
        return networkComponent
    }

    private fun provideVersesComponent(): VersesComponent {
        if (!this::versesComponent.isInitialized) {
            versesComponent = DaggerVersesComponent.factory().create(this)
        }
        return versesComponent
    }

}
