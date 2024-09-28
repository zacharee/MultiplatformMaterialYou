package dev.zwander.compose.libmonet.scheme

import dev.zwander.compose.libmonet.dislike.DislikeAnalyzer.fixIfDisliked
import dev.zwander.compose.libmonet.dynamiccolor.DynamicScheme
import dev.zwander.compose.libmonet.dynamiccolor.Variant
import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.palettes.TonalPalette
import dev.zwander.compose.libmonet.temperature.TemperatureCache
import kotlin.math.max


/**
 * A scheme that places the source color in Scheme.primaryContainer.
 *
 *
 * Primary Container is the source color, adjusted for color relativity. It maintains constant
 * appearance in light mode and dark mode. This adds ~5 tone in light mode, and subtracts ~5 tone in
 * dark mode.
 *
 *
 * Tertiary Container is the complement to the source color, using TemperatureCache. It also
 * maintains constant appearance.
 */
class SchemeFidelity(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) :
    DynamicScheme(
        sourceColorHct,
        Variant.FIDELITY,
        isDark,
        contrastLevel,
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), sourceColorHct.getChroma()),
        TonalPalette.fromHueAndChroma(
            sourceColorHct.getHue(),
            max(sourceColorHct.getChroma() - 32.0, sourceColorHct.getChroma() * 0.5)
        ),
        TonalPalette.fromHct(
            fixIfDisliked(TemperatureCache(sourceColorHct).complement)
        ),
        TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), sourceColorHct.getChroma() / 8.0),
        TonalPalette.fromHueAndChroma(
            sourceColorHct.getHue(), (sourceColorHct.getChroma() / 8.0) + 4.0
        )
    )
