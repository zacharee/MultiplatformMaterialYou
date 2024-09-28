package dev.zwander.compose.libmonet.scheme

import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme
import dev.zwander.compose.libmonet.dynamiccolor.Variant
import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.palettes.TonalPalette
import dev.zwander.compose.libmonet.utils.MathUtils.sanitizeDegreesDouble


/** A calm theme, sedated colors that aren't particularly chromatic.  */
class SchemeTonalSpot(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) :
    DynamicScheme(
        sourceColorHct,
        Variant.TONAL_SPOT,
        isDark,
        contrastLevel,
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 36.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0),
        TonalPalette.fromHueAndChroma(
            sanitizeDegreesDouble(sourceColorHct.getHue() + 60.0), 24.0
        ),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 6.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 8.0)
    )
