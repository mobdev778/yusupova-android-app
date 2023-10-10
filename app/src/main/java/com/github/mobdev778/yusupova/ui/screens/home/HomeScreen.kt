package com.github.mobdev778.yusupova.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.github.mobdev778.yusupova.designsystem.base.YusupovaTheme

@Composable
fun HomeScreen(navController: NavHostController) {
    YusupovaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Hello World", color = YusupovaTheme.scheme.onSurface)
        }
    }
}
