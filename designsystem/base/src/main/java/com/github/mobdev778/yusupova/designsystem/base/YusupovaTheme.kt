package com.github.mobdev778.yusupova.designsystem.base

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

object YusupovaTheme {

    val scheme: ColorScheme
        @Composable
        get() = LocalColorScheme.current
}

@Composable
fun YusupovaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val DarkColorScheme = darkColorScheme(
    background = RawColors.black,

    primary = RawColors.Primary.primary200,
    onPrimary = RawColors.Primary.primary50,

    secondary = RawColors.Complementary.complementary200,
    onSecondary = RawColors.Complementary.complementary700,

    tertiary = RawColors.Analogous.analogous200,
    onTertiary = RawColors.Analogous.analogous800,

    surface = RawColors.Complementary.complementary800,
    onSurface = RawColors.Complementary.complementary200
)

val LightColorScheme = lightColorScheme(
    background = RawColors.white,
    onBackground = RawColors.Primary.primary700,

    primary = RawColors.Primary.primary700,
    onPrimary = RawColors.Primary.primary200,

    secondary = RawColors.Complementary.complementary700,
    onSecondary = RawColors.Complementary.complementary200,

    tertiary = RawColors.Analogous.analogous700,
    onTertiary = RawColors.Analogous.analogous200,

    surface = RawColors.Complementary.complementary200,
    onSurface = RawColors.Complementary.complementary800
)

internal val LocalColorScheme = staticCompositionLocalOf { lightColorScheme() }
