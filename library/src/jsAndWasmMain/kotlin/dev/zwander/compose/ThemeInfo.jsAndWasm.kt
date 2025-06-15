package dev.zwander.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.zwander.compose.libmonet.scheme.ColorScheme

val LocalAccentColor = compositionLocalOf { Color(red = 208, green = 188, blue = 255) }

@Composable
actual fun isSystemInDarkTheme(): Boolean {
    return androidx.compose.foundation.isSystemInDarkTheme()
}

@Composable
actual fun rememberThemeInfo(isDarkMode: Boolean): ThemeInfo {
    return ThemeInfo(
        isDarkMode = isDarkMode,
        colors = ColorScheme(LocalAccentColor.current.toArgb(), isDarkMode).toComposeColorScheme(),
        seedColor = LocalAccentColor.current,
    )
}