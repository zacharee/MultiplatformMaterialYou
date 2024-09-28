package dev.zwander.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.zwander.compose.monet.ColorScheme

val LocalAccentColor = compositionLocalOf { Color(red = 208, green = 188, blue = 255) }

@Composable
actual fun rememberThemeInfo(): ThemeInfo {
    return ThemeInfo(
        isDarkMode = isSystemInDarkTheme(),
        colors = ColorScheme(LocalAccentColor.current.toArgb(), isSystemInDarkTheme()).toComposeColorScheme(),
        seedColor = LocalAccentColor.current,
    )
}