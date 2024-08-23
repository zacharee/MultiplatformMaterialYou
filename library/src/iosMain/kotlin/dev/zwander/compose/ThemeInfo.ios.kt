package dev.zwander.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.LocalUIViewController
import dev.zwander.compose.util.MacOSColors
import dev.zwander.compose.util.TraitEffect
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGFloatVar
import platform.UIKit.UIColor
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.currentTraitCollection
import dev.zwander.compose.monet.ColorScheme

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberThemeInfo(): ThemeInfo {
    val controller = LocalUIViewController.current
    val rootViewController = controller.view.window?.rootViewController

    val rootTint = rootViewController?.view?.tintColor

    val (red, green, blue, alpha) = remember(rootTint) {
        rootTint?.run {
            memScoped {
                val red = alloc<CGFloatVar>()
                val green = alloc<CGFloatVar>()
                val blue = alloc<CGFloatVar>()
                val alpha = alloc<CGFloatVar>()

                val success = getRed(red.ptr, green.ptr, blue.ptr, alpha.ptr)

                if (success) {
                    arrayOf(
                        red.value,
                        green.value,
                        blue.value,
                        alpha.value,
                    )
                } else {
                    arrayOfNulls<CGFloat?>(4)
                }
            }
        } ?: arrayOfNulls(4)
    }

    var style: UIUserInterfaceStyle by remember {
        mutableStateOf(UITraitCollection.currentTraitCollection.userInterfaceStyle)
    }

    val dark by remember {
        derivedStateOf { style == UIUserInterfaceStyle.UIUserInterfaceStyleDark }
    }

    TraitEffect {
        style = UITraitCollection.currentTraitCollection.userInterfaceStyle
    }

    val colorScheme = ColorScheme(
        if (red != null && green != null && blue != null && alpha != null) {
            Color(red.toFloat(), green.toFloat(), blue.toFloat(), alpha.toFloat())
        } else {
            MacOSColors.ACCENT_BLUE
        }.toArgb(),
        dark
    ).toComposeColorScheme()

    val colors = ThemeInfo(
        isDarkMode = dark,
        colors = colorScheme,
    )

    val backgroundColor = colorScheme.background
    val uiColor = UIColor.colorWithRed(
        backgroundColor.red.toDouble(),
        backgroundColor.green.toDouble(),
        backgroundColor.blue.toDouble(),
        backgroundColor.alpha.toDouble(),
    )

    val rv = controller.view.window?.rootViewController?.view
    rv?.backgroundColor = uiColor

    return colors
}