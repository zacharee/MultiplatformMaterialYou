package dev.zwander.compose.libmonet.palettes

import dev.zwander.compose.libmonet.hct.Hct
import kotlin.math.max
import kotlin.math.min


/**
 * An intermediate concept between the key color for a UI theme, and a full color scheme. 5 sets of
 * tones are generated, all except one use the same hue as the key color, and all vary in chroma.
 */
class CorePalette private constructor(argb: Int, isContent: Boolean) {
    val a1: TonalPalette
    val a2: TonalPalette
    val a3: TonalPalette
    val n1: TonalPalette
    val n2: TonalPalette
    val error: TonalPalette

    init {
        val hct = Hct.fromInt(argb)
        val hue = hct.getHue()
        val chroma = hct.getChroma()
        if (isContent) {
            this.a1 = TonalPalette.fromHueAndChroma(hue, chroma)
            this.a2 = TonalPalette.fromHueAndChroma(hue, chroma / 3.0)
            this.a3 = TonalPalette.fromHueAndChroma(hue + 60.0, chroma / 2.0)
            this.n1 = TonalPalette.fromHueAndChroma(hue, min(chroma / 12.0, 4.0))
            this.n2 = TonalPalette.fromHueAndChroma(hue, min(chroma / 6.0, 8.0))
        } else {
            this.a1 = TonalPalette.fromHueAndChroma(hue, max(48.0, chroma))
            this.a2 = TonalPalette.fromHueAndChroma(hue, 16.0)
            this.a3 = TonalPalette.fromHueAndChroma(hue + 60.0, 24.0)
            this.n1 = TonalPalette.fromHueAndChroma(hue, 4.0)
            this.n2 = TonalPalette.fromHueAndChroma(hue, 8.0)
        }
        this.error = TonalPalette.fromHueAndChroma(25.0, 84.0)
    }

    companion object {
        /**
         * Create key tones from a color.
         *
         * @param argb ARGB representation of a color
         */
        fun of(argb: Int): CorePalette {
            return CorePalette(argb, false)
        }

        /**
         * Create content key tones from a color.
         *
         * @param argb ARGB representation of a color
         */
        fun contentOf(argb: Int): CorePalette {
            return CorePalette(argb, true)
        }
    }
}
