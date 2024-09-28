package dev.zwander.compose.libmonet.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme
import kotlin.math.pow
import kotlin.math.roundToInt

fun DynamicScheme.toComposeColorScheme(): ColorScheme {
    val accent1 = primaryPalette
    val accent2 = secondaryPalette
    val accent3 = tertiaryPalette
    val neutral1 = neutralPalette
    val neutral2 = neutralVariantPalette

    return if (isDark) {
        darkColorScheme(
            primary = accent1.shade(200).toColor(),
            onPrimary = accent1.shade(800).toColor(),
            primaryContainer = accent1.shade(700).toColor(),
            onPrimaryContainer = accent1.shade(100).toColor(),
            inversePrimary = accent1.shade(600).toColor(),
            secondary = accent2.shade(200).toColor(),
            onSecondary = accent2.shade(800).toColor(),
            secondaryContainer = accent2.shade(700).toColor(),
            onSecondaryContainer = accent2.shade(100).toColor(),
            tertiary = accent3.shade(200).toColor(),
            onTertiary = accent3.shade(800).toColor(),
            tertiaryContainer = accent3.shade(700).toColor(),
            onTertiaryContainer = accent3.shade(100).toColor(),
            background = neutral1.shade(900).toColor(),
            onBackground = neutral1.shade(100).toColor(),
            surface = neutral1.shade(900).toColor(),
            onSurface = neutral1.shade(100).toColor(),
            surfaceVariant = neutral2.shade(700).toColor(),
            onSurfaceVariant = neutral2.shade(200).toColor(),
            inverseSurface = neutral1.shade(100).toColor(),
            inverseOnSurface = neutral1.shade(800).toColor(),
            outline = neutral2.shade(400).toColor(),
            surfaceContainerLowest = neutral2.shade(600).toColor(),
            surfaceContainer = neutral2.shade(600).toColor().setLuminance(4f),
            surfaceContainerLow = neutral2.shade(900).toColor(),
            surfaceContainerHigh = neutral2.shade(600).toColor().setLuminance(17f),
            surfaceContainerHighest = neutral2.shade(600).toColor().setLuminance(22f),
            outlineVariant = neutral2.shade(700).toColor(),
            scrim = neutral2.shade(1000).toColor(),
            surfaceBright = neutral2.shade(600).toColor().setLuminance(98f),
            surfaceDim = neutral2.shade(600).toColor().setLuminance(6f),
            surfaceTint = accent1.shade(200).toColor(),
        )
    } else {
        lightColorScheme(
            primary = accent1.shade(600).toColor(),
            onPrimary = accent1.shade(0).toColor(),
            primaryContainer = accent1.shade(100).toColor(),
            onPrimaryContainer = accent1.shade(900).toColor(),
            inversePrimary = accent1.shade(200).toColor(),
            secondary = accent2.shade(600).toColor(),
            onSecondary = accent2.shade(0).toColor(),
            secondaryContainer = accent2.shade(100).toColor(),
            onSecondaryContainer = accent2.shade(900).toColor(),
            tertiary = accent3.shade(600).toColor(),
            onTertiary = accent3.shade(0).toColor(),
            tertiaryContainer = accent3.shade(100).toColor(),
            onTertiaryContainer = accent3.shade(900).toColor(),
            background = neutral1.shade(10).toColor(),
            onBackground = neutral1.shade(900).toColor(),
            surface = neutral1.shade(10).toColor(),
            onSurface = neutral1.shade(900).toColor(),
            surfaceVariant = neutral2.shade(100).toColor(),
            onSurfaceVariant = neutral2.shade(700).toColor(),
            inverseSurface = neutral1.shade(800).toColor(),
            inverseOnSurface = neutral1.shade(50).toColor(),
            outline = neutral2.shade(500).toColor(),
            surfaceContainerLowest = neutral2.shade(0).toColor(),
            surfaceContainer = neutral2.shade(600).toColor().setLuminance(94f),
            surfaceContainerLow = neutral2.shade(600).toColor().setLuminance(96f),
            surfaceContainerHigh = neutral2.shade(600).toColor().setLuminance(92f),
            surfaceContainerHighest = neutral2.shade(10-0).toColor(),
            outlineVariant = neutral2.shade(200).toColor(),
            scrim = neutral2.shade(1000).toColor(),
            surfaceBright = neutral2.shade(600).toColor().setLuminance(98f),
            surfaceDim = neutral2.shade(600).toColor().setLuminance(87f),
            surfaceTint = accent1.shade(600).toColor(),
        )
    }
}

private fun Int.toColor(): Color {
    return Color(this)
}

internal fun Color.setLuminance(newLuminance: Float): Color {
    if ((newLuminance < 0.0001) or (newLuminance > 99.9999)) {
        // aRGBFromLstar() from monet ColorUtil.java
        val y = 100 * labInvf((newLuminance + 16) / 116)
        val component = delinearized(y)
        return Color(
            /* red = */ component,
            /* green = */ component,
            /* blue = */ component,
        )
    }

    val sLAB = this.convert(ColorSpaces.CieLab)
    return Color(
        /* luminance = */ newLuminance,
        /* a = */ sLAB.component2(),
        /* b = */ sLAB.component3(),
        colorSpace = ColorSpaces.CieLab
    )
        .convert(ColorSpaces.Srgb)
}

private fun labInvf(ft: Float): Float {
    val e = 216f / 24389f
    val kappa = 24389f / 27f
    val ft3 = ft * ft * ft
    return if (ft3 > e) {
        ft3
    } else {
        (116 * ft - 16) / kappa
    }
}

private fun delinearized(rgbComponent: Float): Int {
    val normalized = rgbComponent / 100
    val delinearized =
        if (normalized <= 0.0031308) {
            normalized * 12.92
        } else {
            1.055 * normalized.toDouble().pow(1.0 / 2.4) - 0.055
        }
    return (delinearized * 255.0).roundToInt().coerceAtLeast(0).coerceAtMost(255)
}
