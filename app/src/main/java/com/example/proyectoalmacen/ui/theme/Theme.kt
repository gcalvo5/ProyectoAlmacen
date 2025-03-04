package com.example.proyectoalmacen.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



private val DarkColorScheme = darkColorScheme(
    // Used for text and icons
    primary = White10,
    secondary = Red30,
    tertiary = Grey60,
    inversePrimary = Black10,

    // Used for containers
    primaryContainer = Black30,
    secondaryContainer = Black50,
    tertiaryContainer = Black80,

    background = Black10,

    //Used as background for loader
    surfaceContainer = Grey60SemiTransparent,

    errorContainer = Red10,
    // SuccessContainer
    surfaceContainerLow = Green20,
    // InfoContainer
    surfaceContainerHigh = Orange30,
    // BlueContainer
    surfaceContainerLowest = Blue50
)

private val LightColorScheme = lightColorScheme(
    // Used for text and icons
    primary = Black10,
    secondary = Red30,
    tertiary = Grey60,
    inversePrimary = White10,
    // Used for containers
    primaryContainer = Grey90,
    secondaryContainer = Grey80,
    tertiaryContainer = Grey60,

    background = White10,

    //Used as background for loader
    surfaceContainer = Grey60SemiTransparent,

    errorContainer = Red70,
    // SuccessContainer
    surfaceContainerLow = Green95,
    // InfoContainer
    surfaceContainerHigh = Orange80,
    // BlueContainer
    surfaceContainerLowest = Blue80
)

@Composable
fun ProyectoAlmacenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme



    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}