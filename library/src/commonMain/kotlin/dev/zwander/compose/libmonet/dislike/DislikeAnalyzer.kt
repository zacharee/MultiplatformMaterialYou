package dev.zwander.compose.libmonet.dislike

import dev.zwander.compose.libmonet.hct.Hct
import kotlin.math.round

/**
 * Check and/or fix universally disliked colors.
 *
 *
 * Color science studies of color preference indicate universal distaste for dark yellow-greens,
 * and also show this is correlated to distate for biological waste and rotting food.
 *
 *
 * See Palmer and Schloss, 2010 or Schloss and Palmer's Chapter 21 in Handbook of Color
 * Psychology (2015).
 */
object DislikeAnalyzer {
    /**
     * Returns true if color is disliked.
     *
     *
     * Disliked is defined as a dark yellow-green that is not neutral.
     */
    fun isDisliked(hct: Hct): Boolean {
        val huePasses = round(hct.getHue()) in 90.0..111.0
        val chromaPasses: Boolean = round(hct.getChroma()) > 16.0
        val tonePasses: Boolean = round(hct.getTone()) < 65.0

        return huePasses && chromaPasses && tonePasses
    }

    /** If color is disliked, lighten it to make it likable.  */
    fun fixIfDisliked(hct: Hct): Hct {
        if (isDisliked(hct)) {
            return Hct.from(hct.getHue(), hct.getChroma(), 70.0)
        }

        return hct
    }
}
