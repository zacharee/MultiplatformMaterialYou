package dev.zwander.compose.libmonet.scheme

import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme
import dev.zwander.compose.libmonet.dynamiccolor.Variant
import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.palettes.TonalPalette


/** A monochrome theme, colors are purely black / white / gray.  */
class SchemeMonochrome(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) :
    DynamicScheme(
        sourceColorHct,
        Variant.MONOCHROME,
        isDark,
        contrastLevel,
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0)
    )
