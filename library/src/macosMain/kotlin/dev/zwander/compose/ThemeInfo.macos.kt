package dev.zwander.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import dev.zwander.compose.libmonet.scheme.ColorScheme
import dev.zwander.compose.util.macOsColorKeyToColor
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSDistributedNotificationCenter
import platform.Foundation.NSNotification

@Composable
actual fun rememberThemeInfo(): ThemeInfo {
    var accentColor by remember {
        mutableStateOf(
            macOsColorKeyToColor(
                NSUserDefaults.standardUserDefaults.objectForKey("AppleAccentColor")?.toString()?.toIntOrNull(),
            )
        )
    }

    var isDark by remember {
        mutableStateOf(
            NSUserDefaults.standardUserDefaults.objectForKey("AppleInterfaceStyle") == "Dark"
        )
    }

    DisposableEffect(null) {
        val observer = { _: NSNotification? ->
            accentColor = macOsColorKeyToColor(
                NSUserDefaults.standardUserDefaults.objectForKey("AppleAccentColor")?.toString()?.toIntOrNull(),
            )
            isDark = NSUserDefaults.standardUserDefaults.objectForKey("AppleInterfaceStyle") == "Dark"
        }

        NSDistributedNotificationCenter.defaultCenter.addObserverForName(
            "AppleInterfaceThemeChangedNotification",
            null,
            null,
            observer,
        )

        onDispose {
            NSDistributedNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    return remember(accentColor, isDark) {
        ThemeInfo(
            isDarkMode = isDark,
            colors = ColorScheme(accentColor.toArgb(), isDark).toComposeColorScheme(),
            seedColor = accentColor,
        )
    }
}