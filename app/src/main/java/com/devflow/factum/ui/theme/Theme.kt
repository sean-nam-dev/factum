package com.devflow.factum.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Green44,
    secondary = Lilac41,
    background = Black06,
    onBackground = White99,
    surface = GrayBlue,

    tertiary = Black06
)

private val LightColorScheme = lightColorScheme(
    primary = Green44,
    secondary = Lilac41,
    background = Black06,
    onBackground = White99,
    surface = GrayBlue,

    tertiary = Black06
)

@Composable
fun FactumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}