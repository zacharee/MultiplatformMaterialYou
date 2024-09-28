package dev.zwander.compose.libmonet.scheme

import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.utils.toComposeColorScheme

enum class Style {
    SPRITZ,
    TONAL_SPOT,
    VIBRANT,
    EXPRESSIVE,
    RAINBOW,
    FRUIT_SALAD,
    CONTENT,
    MONOCHROMATIC,
    FIDELITY,
}

class ColorScheme(
    val seedColor: Int,
    val isDark: Boolean,
    val style: Style = Style.TONAL_SPOT,
    // -1 to 1
    val contrast: Double = 0.0,
) {
    val sourceColorHct = Hct.fromInt(seedColor)
    val scheme = when (style) {
        Style.SPRITZ -> SchemeNeutral(sourceColorHct, isDark, contrast)
        Style.TONAL_SPOT -> SchemeTonalSpot(sourceColorHct, isDark, contrast)
        Style.VIBRANT -> SchemeVibrant(sourceColorHct, isDark, contrast)
        Style.EXPRESSIVE -> SchemeExpressive(sourceColorHct, isDark, contrast)
        Style.RAINBOW -> SchemeRainbow(sourceColorHct, isDark, contrast)
        Style.FRUIT_SALAD -> SchemeFruitSalad(sourceColorHct, isDark, contrast)
        Style.CONTENT -> SchemeContent(sourceColorHct, isDark, contrast)
        Style.MONOCHROMATIC -> SchemeMonochrome(sourceColorHct, isDark, contrast)
        Style.FIDELITY -> SchemeFidelity(sourceColorHct, isDark, contrast)
    }

    fun toComposeColorScheme() = scheme.toComposeColorScheme()
}
