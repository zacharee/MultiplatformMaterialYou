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
import dev.zwander.compose.util.LinuxAccentColorGetter
import dev.zwander.compose.util.UserDefaults
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import dev.zwander.compose.monet.ColorScheme

@Composable
actual fun rememberThemeInfo(): ThemeInfo {
    val (osThemeDetector, isSupported) = remember {
        OsThemeDetector.detector to OsThemeDetector.isSupported
    }

    var dark by remember {
        mutableStateOf(isSupported && osThemeDetector.isDark)
    }

    DisposableEffect(osThemeDetector, isSupported) {
        val listener = { darkMode: Boolean ->
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

    val accentColor = remember {
        val defaultColor = Color(red = 208, green = 188, blue = 255)

        when (hostOs) {
            OS.Windows -> {
                java.awt.Color(
                    Advapi32Util.registryGetIntValue(
                        WinReg.HKEY_CURRENT_USER,
                        "Software\\Microsoft\\Windows\\DWM",
                        "AccentColor",
                    )
                ).rgb
            }
            OS.MacOS -> {
                UserDefaults.standardUserDefaults().getAccentColor().toArgb()
            }
            OS.Linux -> {
                (LinuxAccentColorGetter.getAccentColor() ?: defaultColor).toArgb()
            }
            else -> {
                defaultColor.toArgb()
            }
        }
    }

    val composeColorScheme = remember(accentColor, dark) {
        ColorScheme(accentColor, dark).toComposeColorScheme()
    }

    return remember(composeColorScheme) {
        ThemeInfo(
            isDarkMode = dark,
            colors = composeColorScheme,
        )
    }
}