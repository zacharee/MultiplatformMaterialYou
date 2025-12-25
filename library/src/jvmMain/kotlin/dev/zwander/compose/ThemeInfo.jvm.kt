package dev.zwander.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.jthemedetecor.OsThemeDetector
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import dev.zwander.jfa.appkit.NSUserDefaults
import dev.zwander.compose.libmonet.scheme.ColorScheme
import dev.zwander.compose.util.LinuxAccentColorGetter
import dev.zwander.compose.util.macOsColorKeyToColor
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.util.function.Consumer

@Composable
actual fun isSystemInDarkTheme(): Boolean {
    val (osThemeDetector, isSupported) = remember {
        OsThemeDetector.detector to OsThemeDetector.isSupported
    }

    var dark by remember {
        mutableStateOf(isSupported && osThemeDetector.isDark)
    }

    DisposableEffect(osThemeDetector, isSupported) {
        val listener = Consumer { darkMode: Boolean ->
            dark = darkMode
        }

        if (isSupported) {
            osThemeDetector.registerListener(listener)
        }

        onDispose {
            if (isSupported) {
                osThemeDetector.removeListener(listener)
            }
        }
    }

    return dark
}

@Composable
actual fun rememberThemeInfo(isDarkMode: Boolean): ThemeInfo {
    val accentColor = remember {
        val defaultColor = Color(red = 208, green = 188, blue = 255)

        when (hostOs) {
            OS.Windows -> {
                try {
                    java.awt.Color(
                        Advapi32Util.registryGetIntValue(
                            WinReg.HKEY_CURRENT_USER,
                            "Software\\Microsoft\\Windows\\DWM",
                            "AccentColor",
                        )
                    ).let {
                        // AccentColor is ABGR so we need to swap blue and red.
                        Color(it.blue, it.green, it.red).toArgb()
                    }
                } catch (_: Throwable) {
                    try {
                        Color(
                            Advapi32Util.registryGetIntValue(
                                WinReg.HKEY_CURRENT_USER,
                                "Software\\Microsoft\\Windows\\DWM",
                                "ColorizationColor",
                            )
                        ).toArgb()
                    } catch (_: Throwable) {
                        println("Unable to retrieve Windows accent color.")
                        defaultColor.toArgb()
                    }
                }
            }
            OS.MacOS -> {
                macOsColorKeyToColor(NSUserDefaults.standardUserDefaults().objectForKey("AppleAccentColor")?.toString()?.toIntOrNull()).toArgb()
            }
            OS.Linux -> {
                (LinuxAccentColorGetter.getAccentColor() ?: defaultColor).toArgb()
            }
            else -> {
                defaultColor.toArgb()
            }
        }
    }

    val composeColorScheme = remember(accentColor, isDarkMode) {
        ColorScheme(accentColor, isDarkMode).toComposeColorScheme()
    }

    return remember(composeColorScheme) {
        ThemeInfo(
            isDarkMode = isDarkMode,
            colors = composeColorScheme,
            seedColor = Color(accentColor),
        )
    }
}