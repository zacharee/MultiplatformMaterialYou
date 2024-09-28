package dev.zwander.compose.libmonet.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme

fun DynamicScheme.toComposeColorScheme(): ColorScheme {
    return ColorScheme(
        primary = primary.toColor(),
        onPrimary = onPrimary.toColor(),
        primaryContainer = primaryContainer.toColor(),
        onPrimaryContainer = onPrimaryContainer.toColor(),
        inversePrimary = inversePrimary.toColor(),
        secondary = secondary.toColor(),
        onSecondary = onSecondary.toColor(),
        secondaryContainer = secondaryContainer.toColor(),
        onSecondaryContainer = onSecondaryContainer.toColor(),
        tertiary = tertiary.toColor(),
        onTertiary = onTertiary.toColor(),
        tertiaryContainer = tertiaryContainer.toColor(),
        onTertiaryContainer = onTertiaryContainer.toColor(),
        background = background.toColor(),
        onBackground = onBackground.toColor(),
        surface = surface.toColor(),
        onSurface = onSurface.toColor(),
        surfaceVariant = surfaceVariant.toColor(),
        onSurfaceVariant = onSurfaceVariant.toColor(),
        inverseSurface = inverseSurface.toColor(),
        outline = outline.toColor(),
        surfaceContainerLowest = surfaceContainerLowest.toColor(),
        surfaceContainer = surfaceContainer.toColor(),
        surfaceContainerLow = surfaceContainerLow.toColor(),
        surfaceContainerHigh = surfaceContainerHigh.toColor(),
        surfaceContainerHighest = surfaceContainerHighest.toColor(),
        outlineVariant = outlineVariant.toColor(),
        scrim = scrim.toColor(),
        surfaceBright = surfaceBright.toColor(),
        surfaceDim = surfaceDim.toColor(),
        surfaceTint = surfaceTint.toColor(),
        error = error.toColor(),
        onError = onError.toColor(),
        inverseOnSurface = inverseOnSurface.toColor(),
        onErrorContainer = onErrorContainer.toColor(),
        errorContainer = errorContainer.toColor(),
    )
}

private fun Int.toColor(): Color {
    return Color(this)
}
