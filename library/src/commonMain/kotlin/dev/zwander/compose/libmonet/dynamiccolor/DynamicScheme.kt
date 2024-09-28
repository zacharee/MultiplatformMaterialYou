package dev.zwander.compose.libmonet.dynamiccolor

import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.palettes.TonalPalette
import dev.zwander.compose.libmonet.utils.MathUtils.sanitizeDegreesDouble


/**
 * Provides important settings for creating colors dynamically, and 6 color palettes. Requires: 1. A
 * color. (source color) 2. A theme. (Variant) 3. Whether or not its dark mode. 4. Contrast level.
 * (-1 to 1, currently contrast ratio 3.0 and 7.0)
 */
open class DynamicScheme(
    val sourceColorHct: Hct,
    val variant: Variant,
    val isDark: Boolean,
    val contrastLevel: Double,
    val primaryPalette: TonalPalette,
    val secondaryPalette: TonalPalette,
    val tertiaryPalette: TonalPalette,
    val neutralPalette: TonalPalette,
    val neutralVariantPalette: TonalPalette
) {
    val sourceColorArgb: Int = sourceColorHct.toInt()

    val errorPalette: TonalPalette =
        TonalPalette.fromHueAndChroma(25.0, 84.0)

    fun getHct(dynamicColor: DynamicColor): Hct {
        return dynamicColor.getHct(this)
    }

    fun getArgb(dynamicColor: DynamicColor): Int {
        return dynamicColor.getArgb(this)
    }

    val primaryPaletteKeyColor: Int
        get() = getArgb(MaterialDynamicColors().primaryPaletteKeyColor())

    val secondaryPaletteKeyColor: Int
        get() = getArgb(MaterialDynamicColors().secondaryPaletteKeyColor())

    val tertiaryPaletteKeyColor: Int
        get() = getArgb(MaterialDynamicColors().tertiaryPaletteKeyColor())

    val neutralPaletteKeyColor: Int
        get() = getArgb(MaterialDynamicColors().neutralPaletteKeyColor())

    val neutralVariantPaletteKeyColor: Int
        get() = getArgb(MaterialDynamicColors().neutralVariantPaletteKeyColor())

    val background: Int
        get() = getArgb(MaterialDynamicColors().background())

    val onBackground: Int
        get() = getArgb(MaterialDynamicColors().onBackground())

    val surface: Int
        get() = getArgb(MaterialDynamicColors().surface())

    val surfaceDim: Int
        get() = getArgb(MaterialDynamicColors().surfaceDim())

    val surfaceBright: Int
        get() = getArgb(MaterialDynamicColors().surfaceBright())

    val surfaceContainerLowest: Int
        get() = getArgb(MaterialDynamicColors().surfaceContainerLowest())

    val surfaceContainerLow: Int
        get() = getArgb(MaterialDynamicColors().surfaceContainerLow())

    val surfaceContainer: Int
        get() = getArgb(MaterialDynamicColors().surfaceContainer())

    val surfaceContainerHigh: Int
        get() = getArgb(MaterialDynamicColors().surfaceContainerHigh())

    val surfaceContainerHighest: Int
        get() = getArgb(MaterialDynamicColors().surfaceContainerHighest())

    val onSurface: Int
        get() = getArgb(MaterialDynamicColors().onSurface())

    val surfaceVariant: Int
        get() = getArgb(MaterialDynamicColors().surfaceVariant())

    val onSurfaceVariant: Int
        get() = getArgb(MaterialDynamicColors().onSurfaceVariant())

    val inverseSurface: Int
        get() = getArgb(MaterialDynamicColors().inverseSurface())

    val inverseOnSurface: Int
        get() = getArgb(MaterialDynamicColors().inverseOnSurface())

    val outline: Int
        get() = getArgb(MaterialDynamicColors().outline())

    val outlineVariant: Int
        get() = getArgb(MaterialDynamicColors().outlineVariant())

    val shadow: Int
        get() = getArgb(MaterialDynamicColors().shadow())

    val scrim: Int
        get() = getArgb(MaterialDynamicColors().scrim())

    val surfaceTint: Int
        get() = getArgb(MaterialDynamicColors().surfaceTint())

    val primary: Int
        get() = getArgb(MaterialDynamicColors().primary())

    val onPrimary: Int
        get() = getArgb(MaterialDynamicColors().onPrimary())

    val primaryContainer: Int
        get() = getArgb(MaterialDynamicColors().primaryContainer())

    val onPrimaryContainer: Int
        get() = getArgb(MaterialDynamicColors().onPrimaryContainer())

    val inversePrimary: Int
        get() = getArgb(MaterialDynamicColors().inversePrimary())

    val secondary: Int
        get() = getArgb(MaterialDynamicColors().secondary())

    val onSecondary: Int
        get() = getArgb(MaterialDynamicColors().onSecondary())

    val secondaryContainer: Int
        get() = getArgb(MaterialDynamicColors().secondaryContainer())

    val onSecondaryContainer: Int
        get() = getArgb(MaterialDynamicColors().onSecondaryContainer())

    val tertiary: Int
        get() = getArgb(MaterialDynamicColors().tertiary())

    val onTertiary: Int
        get() = getArgb(MaterialDynamicColors().onTertiary())

    val tertiaryContainer: Int
        get() = getArgb(MaterialDynamicColors().tertiaryContainer())

    val onTertiaryContainer: Int
        get() = getArgb(MaterialDynamicColors().onTertiaryContainer())

    val error: Int
        get() = getArgb(MaterialDynamicColors().error())

    val onError: Int
        get() = getArgb(MaterialDynamicColors().onError())

    val errorContainer: Int
        get() = getArgb(MaterialDynamicColors().errorContainer())

    val onErrorContainer: Int
        get() = getArgb(MaterialDynamicColors().onErrorContainer())

    val primaryFixed: Int
        get() = getArgb(MaterialDynamicColors().primaryFixed())

    val primaryFixedDim: Int
        get() = getArgb(MaterialDynamicColors().primaryFixedDim())

    val onPrimaryFixed: Int
        get() = getArgb(MaterialDynamicColors().onPrimaryFixed())

    val onPrimaryFixedVariant: Int
        get() = getArgb(MaterialDynamicColors().onPrimaryFixedVariant())

    val secondaryFixed: Int
        get() = getArgb(MaterialDynamicColors().secondaryFixed())

    val secondaryFixedDim: Int
        get() = getArgb(MaterialDynamicColors().secondaryFixedDim())

    val onSecondaryFixed: Int
        get() = getArgb(MaterialDynamicColors().onSecondaryFixed())

    val onSecondaryFixedVariant: Int
        get() = getArgb(MaterialDynamicColors().onSecondaryFixedVariant())

    val tertiaryFixed: Int
        get() = getArgb(MaterialDynamicColors().tertiaryFixed())

    val tertiaryFixedDim: Int
        get() = getArgb(MaterialDynamicColors().tertiaryFixedDim())

    val onTertiaryFixed: Int
        get() = getArgb(MaterialDynamicColors().onTertiaryFixed())

    val onTertiaryFixedVariant: Int
        get() = getArgb(MaterialDynamicColors().onTertiaryFixedVariant())

    val controlActivated: Int
        get() = getArgb(MaterialDynamicColors().controlActivated())

    val controlNormal: Int
        get() = getArgb(MaterialDynamicColors().controlNormal())

    val controlHighlight: Int
        get() = getArgb(MaterialDynamicColors().controlHighlight())

    val textPrimaryInverse: Int
        get() = getArgb(MaterialDynamicColors().textPrimaryInverse())

    val textSecondaryAndTertiaryInverse: Int
        get() = getArgb(MaterialDynamicColors().textSecondaryAndTertiaryInverse())

    val textPrimaryInverseDisableOnly: Int
        get() = getArgb(MaterialDynamicColors().textPrimaryInverseDisableOnly())

    val textSecondaryAndTertiaryInverseDisabled: Int
        get() = getArgb(MaterialDynamicColors().textSecondaryAndTertiaryInverseDisabled())

    val textHintInverse: Int
        get() = getArgb(MaterialDynamicColors().textHintInverse())

    companion object {
        /**
         * Given a set of hues and set of hue rotations, locate which hues the source color's hue is
         * between, apply the rotation at the same index as the first hue in the range, and return the
         * rotated hue.
         *
         * @param sourceColorHct The color whose hue should be rotated.
         * @param hues A set of hues.
         * @param rotations A set of hue rotations.
         * @return Color's hue with a rotation applied.
         */
        fun getRotatedHue(sourceColorHct: Hct, hues: DoubleArray, rotations: DoubleArray): Double {
            val sourceHue: Double = sourceColorHct.getHue()
            if (rotations.size == 1) {
                return sanitizeDegreesDouble(sourceHue + rotations[0])
            }
            val size = hues.size
            for (i in 0..(size - 2)) {
                val thisHue = hues[i]
                val nextHue = hues[i + 1]
                if (thisHue < sourceHue && sourceHue < nextHue) {
                    return sanitizeDegreesDouble(sourceHue + rotations[i])
                }
            }
            // If this statement executes, something is wrong, there should have been a rotation
            // found using the arrays.
            return sourceHue
        }
    }
}
