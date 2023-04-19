package com.github.mobdev778.yusupova.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

object NavGraphRegistry {

    private var defaultRoute: String = ""
    private val idScreenMap = LinkedHashMap<String, ComposableContainer>()

    fun getDefaultRoute(): String {
        return defaultRoute
    }

    fun addDefaultScreen(route: String, composable: @Composable (NavHostController) -> Unit) {
        addScreen(route, composable)
        defaultRoute = route
    }

    fun addScreen(route: String, composable: @Composable (NavHostController) -> Unit) {
        idScreenMap[route] = object: ComposableContainer {
            @Composable
            override fun Content(navHostController: NavHostController) {
                composable.invoke(navHostController)
            }
        }
    }

    internal fun getRoutes(): MutableSet<MutableMap.MutableEntry<String, ComposableContainer>> {
        return idScreenMap.entries
    }
}
