package com.github.mobdev778.yusupova.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavGraphRegistry.getDefaultRoute()
    ) {
        NavGraphRegistry.getRoutes().forEach { entry ->
            composable(route = entry.key) {
                val composableContainer = entry.value
                composableContainer.Content(navHostController = navController)
            }
        }
    }
}
