package dev.zwander.compose.libmonet.score

import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.utils.MathUtils.differenceDegrees
import dev.zwander.compose.libmonet.utils.MathUtils.sanitizeDegreesInt
import kotlin.jvm.JvmOverloads
import kotlin.math.floor
import kotlin.math.round


/**
 * Given a large set of colors, remove colors that are unsuitable for a UI theme, and rank the rest
 * based on suitability.
 *
 *
 * Enables use of a high cluster count for image quantization, thus ensuring colors aren't
 * muddied, while curating the high cluster count to a much smaller number of appropriate choices.
 */
object Score {
    private const val TARGET_CHROMA = 48.0 // A1 Chroma
    private const val WEIGHT_PROPORTION = 0.7
    private const val WEIGHT_CHROMA_ABOVE = 0.3
    private const val WEIGHT_CHROMA_BELOW = 0.1
    private const val CUTOFF_CHROMA = 5.0
    private const val CUTOFF_EXCITED_PROPORTION = 0.01

    /**
     * Given a map with keys of colors and values of how often the color appears, rank the colors
     * based on suitability for being used for a UI theme.
     *
     * @param colorsToPopulation map with keys of colors and values of how often the color appears,
     * usually from a source image.
     * @param desired max count of colors to be returned in the list.
     * @param fallbackColorArgb color to be returned if no other options available.
     * @param filter whether to filter out undesireable combinations.
     * @return Colors sorted by suitability for a UI theme. The most suitable color is the first item,
     * the least suitable is the last. There will always be at least one color returned. If all
     * the input colors were not suitable for a theme, a default fallback color will be provided,
     * Google Blue.
     */
    @JvmOverloads
    fun score(
        colorsToPopulation: Map<Int?, Int>,
        desired: Int = 4,
        fallbackColorArgb: Int = -0xbd7a0c,
        filter: Boolean = true
    ): List<Int> {
        // Get the HCT color for each Argb value, while finding the per hue count and
        // total count.

        val colorsHct: MutableList<Hct> = ArrayList()
        val huePopulation = IntArray(360)
        var populationSum = 0.0
        for ((key, value) in colorsToPopulation) {
            val hct = Hct.fromInt(key!!)
            colorsHct.add(hct)
            val hue = floor(hct.getHue()).toInt()
            huePopulation[hue] += value
            populationSum += value.toDouble()
        }

        // Hues with more usage in neighboring 30 degree slice get a larger number.
        val hueExcitedProportions = DoubleArray(360)
        for (hue in 0..359) {
            val proportion = huePopulation[hue] / populationSum
            for (i in hue - 14 until hue + 16) {
                val neighborHue: Int = sanitizeDegreesInt(i)
                hueExcitedProportions[neighborHue] += proportion
            }
        }

        // Scores each HCT color based on usage and chroma, while optionally
        // filtering out values that do not have enough chroma or usage.
        val scoredHcts: MutableList<ScoredHCT> = ArrayList()
        for (hct in colorsHct) {
            val hue: Int = sanitizeDegreesInt(round(hct.getHue()).toInt())
            val proportion = hueExcitedProportions[hue]
            if (filter && (hct.getChroma() < CUTOFF_CHROMA || proportion <= CUTOFF_EXCITED_PROPORTION)) {
                continue
            }

            val proportionScore = proportion * 100.0 * WEIGHT_PROPORTION
            val chromaWeight =
                if (hct.getChroma() < TARGET_CHROMA) WEIGHT_CHROMA_BELOW else WEIGHT_CHROMA_ABOVE
            val chromaScore = (hct.getChroma() - TARGET_CHROMA) * chromaWeight
            val score = proportionScore + chromaScore
            scoredHcts.add(ScoredHCT(hct, score))
        }
        // Sorted so that colors with higher scores come first.
        scoredHcts.sortWith(ScoredComparator())

        // Iterates through potential hue differences in degrees in order to select
        // the colors with the largest distribution of hues possible. Starting at
        // 90 degrees(maximum difference for 4 colors) then decreasing down to a
        // 15 degree minimum.
        val chosenColors: MutableList<Hct> = ArrayList<Hct>()
        for (differenceDegrees in 90 downTo 15) {
            chosenColors.clear()
            for (entry in scoredHcts) {
                val hct = entry.hct
                var hasDuplicateHue = false
                for (chosenHct in chosenColors) {
                    if (differenceDegrees(
                            hct.getHue(),
                            chosenHct.getHue()
                        ) < differenceDegrees
                    ) {
                        hasDuplicateHue = true
                        break
                    }
                }
                if (!hasDuplicateHue) {
                    chosenColors.add(hct)
                }
                if (chosenColors.size >= desired) {
                    break
                }
            }
            if (chosenColors.size >= desired) {
                break
            }
        }
        val colors: MutableList<Int> = ArrayList<Int>()
        if (chosenColors.isEmpty()) {
            colors.add(fallbackColorArgb)
        }
        for (chosenHct in chosenColors) {
            colors.add(chosenHct.toInt())
        }
        return colors
    }

    private class ScoredHCT(val hct: Hct, val score: Double)

    private class ScoredComparator : Comparator<ScoredHCT> {
        override fun compare(a: ScoredHCT, b: ScoredHCT): Int {
            return b.score.compareTo(a.score)
        }
    }
}
