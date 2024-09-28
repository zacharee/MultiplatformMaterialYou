package dev.zwander.compose.libmonet.blend

import dev.zwander.compose.libmonet.hct.Cam16
import dev.zwander.compose.libmonet.hct.Hct
import dev.zwander.compose.libmonet.utils.ColorUtils
import dev.zwander.compose.libmonet.utils.MathUtils.differenceDegrees
import dev.zwander.compose.libmonet.utils.MathUtils.rotationDirection
import dev.zwander.compose.libmonet.utils.MathUtils.sanitizeDegreesDouble
import kotlin.math.min

/** Functions for blending in HCT and CAM16.  */
object Blend {
    /**
     * Blend the design color's HCT hue towards the key color's HCT hue, in a way that leaves the
     * original color recognizable and recognizably shifted towards the key color.
     *
     * @param designColor ARGB representation of an arbitrary color.
     * @param sourceColor ARGB representation of the main theme color.
     * @return The design color with a hue shifted towards the system's color, a slightly
     * warmer/cooler variant of the design color's hue.
     */
    fun harmonize(designColor: Int, sourceColor: Int): Int {
        val fromHct: Hct = Hct.fromInt(designColor)
        val toHct: Hct = Hct.fromInt(sourceColor)
        val differenceDegrees: Double =
            differenceDegrees(fromHct.getHue(), toHct.getHue())
        val rotationDegrees = min(differenceDegrees * 0.5, 15.0)
        val outputHue: Double =
            sanitizeDegreesDouble(
                fromHct.getHue()
                        + rotationDegrees * rotationDirection(
                    fromHct.getHue(),
                    toHct.getHue()
                )
            )
        return Hct.from(outputHue, fromHct.getChroma(), fromHct.getTone()).toInt()
    }

    /**
     * Blends hue from one color into another. The chroma and tone of the original color are
     * maintained.
     *
     * @param from ARGB representation of color
     * @param to ARGB representation of color
     * @param amount how much blending to perform; 0.0 >= and <= 1.0
     * @return from, with a hue blended towards to. Chroma and tone are constant.
     */
    fun hctHue(from: Int, to: Int, amount: Double): Int {
        val ucs = cam16Ucs(from, to, amount)
        val ucsCam: Cam16 = Cam16.fromInt(ucs)
        val fromCam: Cam16 = Cam16.fromInt(from)
        val blended: Hct =
            Hct.from(ucsCam.hue, fromCam.chroma, ColorUtils.lstarFromArgb(from))
        return blended.toInt()
    }

    /**
     * Blend in CAM16-UCS space.
     *
     * @param from ARGB representation of color
     * @param to ARGB representation of color
     * @param amount how much blending to perform; 0.0 >= and <= 1.0
     * @return from, blended towards to. Hue, chroma, and tone will change.
     */
    fun cam16Ucs(from: Int, to: Int, amount: Double): Int {
        val fromCam: Cam16 = Cam16.fromInt(from)
        val toCam: Cam16 = Cam16.fromInt(to)
        val fromJ: Double = fromCam.jstar
        val fromA: Double = fromCam.astar
        val fromB: Double = fromCam.bstar
        val toJ: Double = toCam.jstar
        val toA: Double = toCam.astar
        val toB: Double = toCam.bstar
        val jstar = fromJ + (toJ - fromJ) * amount
        val astar = fromA + (toA - fromA) * amount
        val bstar = fromB + (toB - fromB) * amount
        return Cam16.fromUcs(jstar, astar, bstar).toInt()
    }
}
