package com.nps.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF07C160),
    primaryVariant = Color(0xFF086C38),
    secondary = Color(0xFF538DEF),
    surface = Color(0xFFF2F2F2),
    background = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF07C160),
    primaryVariant = Color(0xFF086C38),
    secondary = Color(0xFF538DEF),
    surface = Color(0xFFF2F2F2),
    background = Color.White
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun DesktopNpsComposeTheme(darkTheme: Boolean = false/*isSystemInDarkTheme()*/, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}