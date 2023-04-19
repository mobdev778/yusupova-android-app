package com.github.mobdev778.yusupova.app

import android.app.Application
import com.github.mobdev778.yusupova.navigation.NavGraphRegistry
import com.github.mobdev778.yusupova.navigation.Screen
import com.github.mobdev778.yusupova.ui.screens.home.HomeScreen
import com.github.mobdev778.yusupova.ui.screens.splash.SplashScreen

class YusupovaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initNavigationGraph()
    }

    private fun initNavigationGraph() {
        NavGraphRegistry.addDefaultScreen(Screen.Splash.route) { navController -> SplashScreen(navController) }
        NavGraphRegistry.addScreen(Screen.Home.route) { navController -> HomeScreen(navController) }
    }
}
