package com.github.mobdev778.yusupova.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.github.mobdev778.yusupova.navigation.NavGraph
import com.github.mobdev778.yusupova.ui.theme.YusupovaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YusupovaTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

