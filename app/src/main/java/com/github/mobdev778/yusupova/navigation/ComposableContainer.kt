package com.github.mobdev778.yusupova.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

internal interface ComposableContainer {

    @Composable
    fun Content(navHostController: NavHostController)
}
