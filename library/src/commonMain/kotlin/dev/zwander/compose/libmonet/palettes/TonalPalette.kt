package dev.zwander.compose.libmonet.palettes

import dev.zwander.compose.libmonet.hct.Hct
import kotlin.math.abs
import kotlin.math.round


/**
 * A convenience class for retrieving colors that are constant in hue and chroma, but vary in tone.
 */
class TonalPalette private constructor(
    /** The hue of the Tonal Palette, in HCT. Ranges from 0 to 360.  */
    var hue: Double,
    /** The chroma of the Tonal Palette, in HCT. Ranges from 0 to ~130 (for sRGB gamut).  */
    var chroma: Double,
    /** The key color is the first tone, starting from T50, that matches the palette's chroma.  */
    var keyColor: Hct
) {
    var cache: MutableMap<Int, Int> = HashMap()

    /**
     * Create an ARGB color with HCT hue and chroma of this Tones instance, and the provided HCT tone.
     *
     * @param tone HCT tone, measured from 0 to 100.
     * @return ARGB representation of a color with that tone.
     */
    // AndroidJdkLibsChecker is higher priority than ComputeIfAbsentUseValue (b/119581923)
    fun tone(tone: Int): Int {
        var color = cache[tone]
        if (color == null) {
            color = Hct.from(this.hue, this.chroma, tone.toDouble()).toInt()
            cache[tone] = color
        }
        return color
    }

    /** Given a tone, use hue and chroma of palette to create a color, and return it as HCT.  */
    fun getHct(tone: Double): Hct {
        return Hct.from(this.hue, this.chroma, tone)
    }

    companion object {
        /**
         * Create tones using the HCT hue and chroma from a color.
         *
         * @param argb ARGB representation of a color
         * @return Tones matching that color's hue and chroma.
         */
        fun fromInt(argb: Int): TonalPalette {
            return fromHct(Hct.fromInt(argb))
        }

        /**
         * Create tones using a HCT color.
         *
         * @param hct HCT representation of a color.
         * @return Tones matching that color's hue and chroma.
         */
        fun fromHct(hct: Hct): TonalPalette {
            return TonalPalette(hct.getHue(), hct.getChroma(), hct)
        }

        /**
         * Create tones from a defined HCT hue and chroma.
         *
         * @param hue HCT hue
         * @param chroma HCT chroma
         * @return Tones matching hue and chroma.
         */
        fun fromHueAndChroma(hue: Double, chroma: Double): TonalPalette {
            return TonalPalette(hue, chroma, createKeyColor(hue, chroma))
        }

        /** The key color is the first tone, starting from T50, matching the given hue and chroma.  */
        private fun createKeyColor(hue: Double, chroma: Double): Hct {
            val startTone = 50.0
            var smallestDeltaHct = Hct.from(hue, chroma, startTone)
            var smallestDelta = abs(smallestDeltaHct.getChroma() - chroma)
            // Starting from T50, check T+/-delta to see if they match the requested
            // chroma.
            //
            // Starts from T50 because T50 has the most chroma available, on
            // average. Thus it is most likely to have a direct answer and minimize
            // iteration.
            var delta = 1.0
            while (delta < 50.0) {
                // Termination condition rounding instead of minimizing delta to avoid
                // case where requested chroma is 16.51, and the closest chroma is 16.49.
                // Error is minimized, but when rounded and displayed, requested chroma
                // is 17, key color's chroma is 16.
                if (round(chroma) == round(smallestDeltaHct.getChroma())) {
                    return smallestDeltaHct
                }

                val hctAdd = Hct.from(hue, chroma, startTone + delta)
                val hctAddDelta = abs(hctAdd.getChroma() - chroma)
                if (hctAddDelta < smallestDelta) {
                    smallestDelta = hctAddDelta
                    smallestDeltaHct = hctAdd
                }

                val hctSubtract = Hct.from(hue, chroma, startTone - delta)
                val hctSubtractDelta = abs(hctSubtract.getChroma() - chroma)
                if (hctSubtractDelta < smallestDelta) {
                    smallestDelta = hctSubtractDelta
                    smallestDeltaHct = hctSubtract
                }
                delta += 1.0
            }

            return smallestDeltaHct
        }
    }
}
