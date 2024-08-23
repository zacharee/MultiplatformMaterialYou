package dev.zwander.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

data class ThemeInfo(
    val isDarkMode: Boolean,
    val colors: ColorScheme,
)

@Composable
expect fun rememberThemeInfo(): ThemeInfo

@Suppress("unused")
@Composable
fun DynamicMaterialTheme(
    content: @Composable () -> Unit,
) {
    val themeInfo = rememberThemeInfo()

    MaterialTheme(
        content = content,
        colorScheme = themeInfo.colors,
    )
}