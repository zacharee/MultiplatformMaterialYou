package dev.zwander.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ThemeInfo(
    val isDarkMode: Boolean,
    val colors: ColorScheme,
    val seedColor: Color,
)

@Composable
expect fun rememberThemeInfo(isDarkMode: Boolean = isSystemInDarkTheme()): ThemeInfo

@Composable
expect fun isSystemInDarkTheme(): Boolean

@Suppress("unused")
@Composable
fun DynamicMaterialTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeInfo = rememberThemeInfo(isDarkMode = isDarkMode)

    MaterialTheme(
        content = content,
        colorScheme = themeInfo.colors,
    )
}