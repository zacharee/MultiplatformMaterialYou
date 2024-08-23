@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "EXPOSED_PARAMETER_TYPE")
@file:JvmName("ThemeInfoAndroid")

package dev.zwander.compose

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.dynamicTonalPalette
import androidx.compose.material3.dynamicDarkColorScheme31
import androidx.compose.material3.dynamicLightColorScheme31
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberThemeInfo(): ThemeInfo {
    val context = LocalContext.current

    val isAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val dark = isSystemInDarkTheme()
    val isOneUI = remember {
        context.packageManager.hasSystemFeature("com.samsung.feature.samsung_experience_mobile")
    }
    val colorScheme = remember(dark, isAndroid12) {
        if (dark) {
            if (isAndroid12) {
                if (isOneUI) {
                    dynamicDarkColorScheme31(dynamicTonalPalette(context))
                } else {
                    dynamicDarkColorScheme(context)
                }
            } else {
                darkColorScheme()
            }
        } else {
            if (isAndroid12) {
                if (isOneUI) {
                    dynamicLightColorScheme31(dynamicTonalPalette(context))
                } else {
                    dynamicLightColorScheme(context)
                }
            } else {
                lightColorScheme()
            }
        }
    }

    return remember(colorScheme) {
        ThemeInfo(
            isDarkMode = dark,
            colors = colorScheme,
        )
    }
}