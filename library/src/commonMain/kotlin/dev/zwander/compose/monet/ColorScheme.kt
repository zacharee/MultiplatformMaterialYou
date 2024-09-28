@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.zwander.compose.monet

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

const val ACCENT1_CHROMA = 48.0f
const val GOOGLE_BLUE = 0xFF1b6ef3.toInt()
const val MIN_CHROMA = 5

internal interface Hue {
    fun get(sourceColor: Cam): Double

    /**
     * Given a hue, and a mapping of hues to hue rotations, find which hues in the mapping the
     * hue fall betweens, and use the hue rotation of the lower hue.
     *
     * @param sourceHue hue of source color
     * @param hueAndRotations list of pairs, where the first item in a pair is a hue, and the
     *    second item in the pair is a hue rotation that should be applied
     */
    fun getHueRotation(sourceHue: Double, hueAndRotations: List<Pair<Int, Int>>): Double {
        val sanitizedSourceHue = (if (sourceHue < 0 || sourceHue >= 360) 0.0 else sourceHue)
        for (i in 0..hueAndRotations.size - 2) {
            val thisHue = hueAndRotations[i].first
            val nextHue = hueAndRotations[i + 1].first
            if (thisHue <= sanitizedSourceHue && sanitizedSourceHue < nextHue) {
                return ColorScheme.wrapDegreesDouble(sanitizedSourceHue + hueAndRotations[i].second)
            }
        }

        // If this statement executes, something is wrong, there should have been a rotation
        // found using the arrays.
        return sourceHue
    }
}

internal class HueSource : Hue {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.hue
    }
}

internal class HueAdd(val amountDegrees: Double) : Hue {
    override fun get(sourceColor: Cam): Double {
        return ColorScheme.wrapDegreesDouble(sourceColor.hue + amountDegrees)
    }
}

internal class HueSubtract(val amountDegrees: Double) : Hue {
    override fun get(sourceColor: Cam): Double {
        return ColorScheme.wrapDegreesDouble(sourceColor.hue - amountDegrees)
    }
}

internal class HueVibrantSecondary : Hue {
    val hueToRotations = listOf(Pair(0, 18), Pair(41, 15), Pair(61, 10), Pair(101, 12),
            Pair(131, 15), Pair(181, 18), Pair(251, 15), Pair(301, 12), Pair(360, 12))
    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

internal class HueVibrantTertiary : Hue {
    val hueToRotations = listOf(Pair(0, 35), Pair(41, 30), Pair(61, 20), Pair(101, 25),
            Pair(131, 30), Pair(181, 35), Pair(251, 30), Pair(301, 25), Pair(360, 25))
    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

internal class HueExpressiveSecondary : Hue {
    val hueToRotations = listOf(Pair(0, 45), Pair(21, 95), Pair(51, 45), Pair(121, 20),
            Pair(151, 45), Pair(191, 90), Pair(271, 45), Pair(321, 45), Pair(360, 45))
    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

internal class HueExpressiveTertiary : Hue {
    val hueToRotations = listOf(Pair(0, 120), Pair(21, 120), Pair(51, 20), Pair(121, 45),
            Pair(151, 20), Pair(191, 15), Pair(271, 20), Pair(321, 120), Pair(360, 120))
    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

internal interface Chroma {
    fun get(sourceColor: Cam): Double
}

internal class ChromaMaxOut : Chroma {
    override fun get(sourceColor: Cam): Double {
        // Intentionally high. Gamut mapping from impossible HCT to sRGB will ensure that
        // the maximum chroma is reached, even if lower than this constant.
        return 130.0
    }
}

internal class ChromaMultiple(val multiple: Double) : Chroma {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.chroma * multiple
    }
}

internal class ChromaConstant(val chroma: Double) : Chroma {
    override fun get(sourceColor: Cam): Double {
        return chroma
    }
}

internal class ChromaSource : Chroma {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.chroma
    }
}

internal class TonalSpec(val hue: Hue = HueSource(), val chroma: Chroma) {
    fun shades(sourceColor: Cam): List<Int> {
        val hue = hue.get(sourceColor)
        val chroma = chroma.get(sourceColor)
        return Shades.of(hue, chroma).toList()
    }
}

internal class CoreSpec(
    val a1: TonalSpec,
    val a2: TonalSpec,
    val a3: TonalSpec,
    val n1: TonalSpec,
    val n2: TonalSpec
)

enum class Style(internal val coreSpec: CoreSpec) {
    SPRITZ(CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(12.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(8.0)),
            a3 = TonalSpec(HueSource(), ChromaConstant(16.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(2.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(2.0))
    )),
    TONAL_SPOT(CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(36.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(16.0)),
            a3 = TonalSpec(HueAdd(60.0), ChromaConstant(24.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(4.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(8.0))
    )),
    VIBRANT(CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaMaxOut()),
            a2 = TonalSpec(HueVibrantSecondary(), ChromaConstant(24.0)),
            a3 = TonalSpec(HueVibrantTertiary(), ChromaConstant(32.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(10.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(12.0))
    )),
    EXPRESSIVE(CoreSpec(
            a1 = TonalSpec(HueAdd(240.0), ChromaConstant(40.0)),
            a2 = TonalSpec(HueExpressiveSecondary(), ChromaConstant(24.0)),
            a3 = TonalSpec(HueExpressiveTertiary(), ChromaConstant(32.0)),
            n1 = TonalSpec(HueAdd(15.0), ChromaConstant(8.0)),
            n2 = TonalSpec(HueAdd(15.0), ChromaConstant(12.0))
    )),
    RAINBOW(CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(48.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(16.0)),
            a3 = TonalSpec(HueAdd(60.0), ChromaConstant(24.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(0.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(0.0))
    )),
    FRUIT_SALAD(CoreSpec(
            a1 = TonalSpec(HueSubtract(50.0), ChromaConstant(48.0)),
            a2 = TonalSpec(HueSubtract(50.0), ChromaConstant(36.0)),
            a3 = TonalSpec(HueSource(), ChromaConstant(36.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(10.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(16.0))
    )),
    CONTENT(CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaSource()),
            a2 = TonalSpec(HueSource(), ChromaMultiple(0.33)),
            a3 = TonalSpec(HueSource(), ChromaMultiple(0.66)),
            n1 = TonalSpec(HueSource(), ChromaMultiple(0.0833)),
            n2 = TonalSpec(HueSource(), ChromaMultiple(0.1666))
    )),
}

class ColorScheme(
    val seed: Int,
    val darkTheme: Boolean,
    val style: Style = Style.TONAL_SPOT,
) {
    val accent1: List<Int>
    val accent2: List<Int>
    val accent3: List<Int>
    val neutral1: List<Int>
    val neutral2: List<Int>

    constructor(seed: Int, darkTheme: Boolean):
            this(seed, darkTheme, Style.TONAL_SPOT)

    constructor(
        wallpaperColors: WallpaperColors,
        darkTheme: Boolean,
        style: Style = Style.TONAL_SPOT
    ): this(getSeedColor(wallpaperColors, style != Style.CONTENT), darkTheme, style)

    val allAccentColors: List<Int>
        get() {
            val allColors = mutableListOf<Int>()
            allColors.addAll(accent1)
            allColors.addAll(accent2)
            allColors.addAll(accent3)
            return allColors
        }

    val allNeutralColors: List<Int>
        get() {
            val allColors = mutableListOf<Int>()
            allColors.addAll(neutral1)
            allColors.addAll(neutral2)
            return allColors
        }

    val backgroundColor
        get() = ColorUtils.setAlphaComponent(if (darkTheme) neutral1[8] else neutral1[0], 0xFF)

    val accentColor
        get() = ColorUtils.setAlphaComponent(if (darkTheme) accent1[2] else accent1[6], 0xFF)

    init {
        val proposedSeedCam = Cam.fromInt(seed)
        val seedArgb = if (seed == Color.Transparent.toArgb()) {
            GOOGLE_BLUE
        } else if (style != Style.CONTENT && proposedSeedCam.chroma < 5) {
            GOOGLE_BLUE
        } else {
            seed
        }
        val camSeed = Cam.fromInt(seedArgb)
        accent1 = style.coreSpec.a1.shades(camSeed)
        accent2 = style.coreSpec.a2.shades(camSeed)
        accent3 = style.coreSpec.a3.shades(camSeed)
        neutral1 = style.coreSpec.n1.shades(camSeed)
        neutral2 = style.coreSpec.n2.shades(camSeed)
    }

    override fun toString(): String {
        return "ColorScheme {\n" +
                "  seed color: ${stringForColor(seed)}\n" +
                "  style: $style\n" +
                "  palettes: \n" +
                "  ${humanReadable("PRIMARY", accent1)}\n" +
                "  ${humanReadable("SECONDARY", accent2)}\n" +
                "  ${humanReadable("TERTIARY", accent3)}\n" +
                "  ${humanReadable("NEUTRAL", neutral1)}\n" +
                "  ${humanReadable("NEUTRAL VARIANT", neutral2)}\n" +
                "}"
    }

    fun toComposeColorScheme(): androidx.compose.material3.ColorScheme {
        val paletteSize = accent1.size

        fun getColor(type: Type, paletteIndex: Int, num: Int): Color {
            val shades = when (type) {
                Type.Accent -> allAccentColors
                Type.Neutral -> allNeutralColors
            }

            val lum = when (num) {
                10 -> 0
                50 -> 1
                else -> 1 + num / 100
            }

            return Color(
                shades.filterIndexed { index, _ ->
                    val l = index % paletteSize
                    val idx = index / paletteSize + 1

                    l == lum && idx == paletteIndex
                }.first()
            )
        }

        return if (darkTheme) {
            darkColorScheme(
                primary = getColor(Type.Accent, 1, 200),
                onPrimary = getColor(Type.Accent, 1, 800),
                primaryContainer = getColor(Type.Accent, 1, 700),
                onPrimaryContainer = getColor(Type.Accent, 1, 100),
                inversePrimary = getColor(Type.Accent, 1, 600),
                secondary = getColor(Type.Accent, 2, 200),
                onSecondary = getColor(Type.Accent, 2, 800),
                secondaryContainer = getColor(Type.Accent, 2, 700),
                onSecondaryContainer = getColor(Type.Accent, 2, 100),
                tertiary = getColor(Type.Accent, 3, 200),
                onTertiary = getColor(Type.Accent, 3, 800),
                tertiaryContainer = getColor(Type.Accent, 3, 700),
                onTertiaryContainer = getColor(Type.Accent, 3, 100),
                background = getColor(Type.Neutral, 1, 900),
                onBackground = getColor(Type.Neutral, 1, 100),
                surface = getColor(Type.Neutral, 1, 900),
                onSurface = getColor(Type.Neutral, 1, 100),
                surfaceVariant = getColor(Type.Neutral, 2, 700),
                onSurfaceVariant = getColor(Type.Neutral, 2, 200),
                inverseSurface = getColor(Type.Neutral, 1, 100),
                inverseOnSurface = getColor(Type.Neutral, 1, 800),
                outline = getColor(Type.Neutral, 2, 400),
                surfaceContainerLowest = getColor(Type.Neutral, 2, 600),
                surfaceContainer = getColor(Type.Neutral, 2, 600).setLuminance(4f),
                surfaceContainerLow = getColor(Type.Neutral, 2, 900),
                surfaceContainerHigh = getColor(Type.Neutral, 2, 600).setLuminance(17f),
                outlineVariant = getColor(Type.Neutral, 2, 700),
                scrim = getColor(Type.Neutral, 2, 1000),
                surfaceBright = getColor(Type.Neutral, 2, 600).setLuminance(98f),
                surfaceDim = getColor(Type.Neutral, 2, 600).setLuminance(6f),
                surfaceTint = getColor(Type.Accent, 1, 200),
                surfaceContainerHighest = getColor(Type.Neutral, 2, 600).setLuminance(22f),
            )
        } else {
            lightColorScheme(
                primary = getColor(Type.Accent, 1, 600),
                onPrimary = getColor(Type.Accent, 1, 0),
                primaryContainer = getColor(Type.Accent, 1, 100),
                onPrimaryContainer = getColor(Type.Accent, 1, 900),
                inversePrimary = getColor(Type.Accent, 1, 200),
                secondary = getColor(Type.Accent, 2, 600),
                onSecondary = getColor(Type.Accent, 2, 0),
                secondaryContainer = getColor(Type.Accent, 2, 100),
                onSecondaryContainer = getColor(Type.Accent, 2, 900),
                tertiary = getColor(Type.Accent, 3, 600),
                onTertiary = getColor(Type.Accent, 3, 0),
                tertiaryContainer = getColor(Type.Accent, 3, 100),
                onTertiaryContainer = getColor(Type.Accent, 3, 900),
                background = getColor(Type.Neutral, 1, 10),
                onBackground = getColor(Type.Neutral, 1, 900),
                surface = getColor(Type.Neutral, 1, 10),
                onSurface = getColor(Type.Neutral, 1, 900),
                surfaceVariant = getColor(Type.Neutral, 2, 100),
                onSurfaceVariant = getColor(Type.Neutral, 2, 700),
                inverseSurface = getColor(Type.Neutral, 1, 800),
                inverseOnSurface = getColor(Type.Neutral, 1, 50),
                outline = getColor(Type.Neutral, 2, 500),
                surfaceContainerLowest = getColor(Type.Neutral, 2, 0),
                surfaceContainer = getColor(Type.Neutral, 2, 600).setLuminance(94f),
                surfaceContainerLow = getColor(Type.Neutral, 2, 600).setLuminance(96f),
                surfaceContainerHigh = getColor(Type.Neutral, 2, 600).setLuminance(92f),
                outlineVariant = getColor(Type.Neutral, 2, 200),
                scrim = getColor(Type.Neutral, 2, 1000),
                surfaceBright = getColor(Type.Neutral, 2, 600).setLuminance(98f),
                surfaceDim = getColor(Type.Neutral, 2, 600).setLuminance(87f),
                surfaceTint = getColor(Type.Accent, 1, 600),
                surfaceContainerHighest = getColor(Type.Neutral, 2, 100),
            )
        }
    }

    internal fun Color.setLuminance(newLuminance: Float): Color {
        if ((newLuminance < 0.0001) or (newLuminance > 99.9999)) {
            // aRGBFromLstar() from monet ColorUtil.java
            val y = 100 * labInvf((newLuminance + 16) / 116)
            val component = delinearized(y)
            return Color(
                /* red = */ component,
                /* green = */ component,
                /* blue = */ component,
            )
        }

        val sLAB = this.convert(ColorSpaces.CieLab)
        return Color(
            /* luminance = */ newLuminance,
            /* a = */ sLAB.component2(),
            /* b = */ sLAB.component3(),
            colorSpace = ColorSpaces.CieLab
        )
            .convert(ColorSpaces.Srgb)
    }

    private fun labInvf(ft: Float): Float {
        val e = 216f / 24389f
        val kappa = 24389f / 27f
        val ft3 = ft * ft * ft
        return if (ft3 > e) {
            ft3
        } else {
            (116 * ft - 16) / kappa
        }
    }

    private fun delinearized(rgbComponent: Float): Int {
        val normalized = rgbComponent / 100
        val delinearized =
            if (normalized <= 0.0031308) {
                normalized * 12.92
            } else {
                1.055 * normalized.toDouble().pow(1.0 / 2.4) - 0.055
            }
        return (delinearized * 255.0).roundToInt().coerceAtLeast(0).coerceAtMost(255)
    }

    companion object {
        enum class Type {
            Accent,
            Neutral,
        }

        /**
         * Identifies a color to create a color scheme from.
         *
         * @param wallpaperColors Colors extracted from an image via quantization.
         * @param filter If false, allow colors that have low chroma, creating grayscale themes.
         * @return ARGB int representing the color
         */
        fun getSeedColor(wallpaperColors: WallpaperColors, filter: Boolean = true): Int {
            return getSeedColors(wallpaperColors, filter).first()
        }

        /**
         * Filters and ranks colors from WallpaperColors.
         *
         * @param wallpaperColors Colors extracted from an image via quantization.
         * @param filter If false, allow colors that have low chroma, creating grayscale themes.
         * @return List of ARGB ints, ordered from highest scoring to lowest.
         */
        fun getSeedColors(wallpaperColors: WallpaperColors, filter: Boolean = true): List<Int> {
            val totalPopulation = wallpaperColors.allColors.values.reduce { a, b -> a + b }
                    
            val totalPopulationMeaningless = (totalPopulation.toDouble() == 0.0)
            if (totalPopulationMeaningless) {
                // WallpaperColors with a population of 0 indicate the colors didn't come from
                // quantization. Instead of scoring, trust the ordering of the provided primary
                // secondary/tertiary colors.
                //
                // In this case, the colors are usually from a Live Wallpaper.
                val distinctColors = wallpaperColors.mainColors.map {
                    it.toArgb()
                }.distinct().filter {
                    !filter || Cam.fromInt(it).chroma >= MIN_CHROMA
                }.toList()
                if (distinctColors.isEmpty()) {
                    return listOf(GOOGLE_BLUE)
                }
                return distinctColors
            }

            val intToProportion = wallpaperColors.allColors.mapValues {
                it.value / totalPopulation.toDouble()
            }
            val intToCam = wallpaperColors.allColors.mapValues { Cam.fromInt(it.key) }

            // Get an array with 360 slots. A slot contains the percentage of colors with that hue.
            val hueProportions = huePopulations(intToCam, intToProportion, filter)
            // Map each color to the percentage of the image with its hue.
            val intToHueProportion = wallpaperColors.allColors.mapValues {
                val cam = intToCam[it.key]!!
                val hue = cam.hue.roundToInt()
                var proportion = 0.0
                for (i in hue - 15..hue + 15) {
                    proportion += hueProportions[wrapDegrees(i)]
                }
                proportion
            }
            // Remove any inappropriate seed colors. For example, low chroma colors look grayscale
            // raising their chroma will turn them to a much louder color that may not have been
            // in the image.
            val filteredIntToCam = if (!filter) intToCam else (intToCam.filter {
                val cam = it.value
                val proportion = intToHueProportion[it.key]!!
                cam.chroma >= MIN_CHROMA &&
                        (totalPopulationMeaningless || proportion > 0.01)
            })
            // Sort the colors by score, from high to low.
            val intToScoreIntermediate = filteredIntToCam.mapValues {
                score(it.value, intToHueProportion[it.key]!!)
            }
            val intToScore = intToScoreIntermediate.entries.toMutableList()
            intToScore.sortByDescending { it.value }

            // Go through the colors, from high score to low score.
            // If the color is distinct in hue from colors picked so far, pick the color.
            // Iteratively decrease the amount of hue distinctness required, thus ensuring we
            // maximize difference between colors.
            val minimumHueDistance = 15
            val seeds = mutableListOf<Int>()
            maximizeHueDistance@ for (i in 90 downTo minimumHueDistance step 1) {
                seeds.clear()
                for (entry in intToScore) {
                    val int = entry.key
                    val existingSeedNearby = seeds.find {
                        val hueA = intToCam[int]!!.hue
                        val hueB = intToCam[it]!!.hue
                        hueDiff(hueA, hueB) < i } != null
                    if (existingSeedNearby) {
                        continue
                    }
                    seeds.add(int)
                    if (seeds.size >= 4) {
                        break@maximizeHueDistance
                    }
                }
            }

            if (seeds.isEmpty()) {
                // Use gBlue 500 if there are 0 colors
                seeds.add(GOOGLE_BLUE)
            }

            return seeds
        }

        private fun wrapDegrees(degrees: Int): Int {
            return when {
                degrees < 0 -> {
                    (degrees % 360) + 360
                }
                degrees >= 360 -> {
                    degrees % 360
                }
                else -> {
                    degrees
                }
            }
        }

        fun wrapDegreesDouble(degrees: Double): Double {
            return when {
                degrees < 0 -> {
                    (degrees % 360) + 360
                }
                degrees >= 360 -> {
                    degrees % 360
                }
                else -> {
                    degrees
                }
            }
        }

        private fun hueDiff(a: Double, b: Double): Double {
            return 180.0 - ((a - b).absoluteValue - 180.0).absoluteValue
        }

        @OptIn(ExperimentalStdlibApi::class)
        private fun stringForColor(color: Int): String {
            val width = 4
            val hct = Cam.fromInt(color)
            val h = "H${hct.hue.roundToInt().toString().padEnd(width)}"
            val c = "C${hct.chroma.roundToInt().toString().padEnd(width)}"
            val t = "T${CamUtils.lstarFromInt(color).roundToInt().toString().padEnd(width)}"
            val hex = (color and 0xffffff).toHexString().uppercase()
            return "$h$c$t = #$hex"
        }

        private fun humanReadable(paletteName: String, colors: List<Int>): String {
            return "$paletteName\n" + colors.map {
                stringForColor(it)
            }.joinToString(separator = "\n") { it }
        }

        private fun score(cam: Cam, proportion: Double): Double {
            val proportionScore = 0.7 * 100.0 * proportion
            val chromaScore = if (cam.chroma < ACCENT1_CHROMA) 0.1 * (cam.chroma - ACCENT1_CHROMA)
            else 0.3 * (cam.chroma - ACCENT1_CHROMA)
            return chromaScore + proportionScore
        }

        private fun huePopulations(
            camByColor: Map<Int, Cam>,
            populationByColor: Map<Int, Double>,
            filter: Boolean = true
        ): List<Double> {
            val huePopulation = List(size = 360, init = { 0.0 }).toMutableList()

            for (entry in populationByColor.entries) {
                val population = populationByColor[entry.key]!!
                val cam = camByColor[entry.key]!!
                val hue = cam.hue.roundToInt() % 360
                if (filter && cam.chroma <= MIN_CHROMA) {
                    continue
                }
                huePopulation[hue] = huePopulation[hue] + population
            }

            return huePopulation
        }
    }
}