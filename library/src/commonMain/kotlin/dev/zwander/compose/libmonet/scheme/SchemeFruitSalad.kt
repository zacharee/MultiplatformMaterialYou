package dev.zwander.compose.libmonet.scheme

import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme
import dev.zwander.compose.libmonet.dynamiccolor.Variant
import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.palettes.TonalPalette
import dev.zwander.compose.libmonet.utils.MathUtils.sanitizeDegreesDouble


/** A playful theme - the source color's hue does not appear in the theme.  */
class SchemeFruitSalad(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) :
    DynamicScheme(
        sourceColorHct,
        Variant.FRUIT_SALAD,
        isDark,
        contrastLevel,
        TonalPalette.fromHueAndChroma(
            sanitizeDegreesDouble(sourceColorHct.getHue() - 50.0), 48.0
        ),
        TonalPalette.fromHueAndChroma(
            sanitizeDegreesDouble(sourceColorHct.getHue() - 50.0), 36.0
        ),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 36.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 10.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0)
    )