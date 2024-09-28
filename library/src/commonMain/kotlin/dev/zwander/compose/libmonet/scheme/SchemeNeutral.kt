package dev.zwander.compose.libmonet.scheme

import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme
import dev.zwander.compose.libmonet.dynamiccolor.Variant
import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.palettes.TonalPalette


/** A theme that's slightly more chromatic than monochrome, which is purely black / white / gray.  */
class SchemeNeutral(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) :
    DynamicScheme(
        sourceColorHct,
        Variant.NEUTRAL,
        isDark,
        contrastLevel,
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 12.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 8.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 2.0),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 2.0)
    )
