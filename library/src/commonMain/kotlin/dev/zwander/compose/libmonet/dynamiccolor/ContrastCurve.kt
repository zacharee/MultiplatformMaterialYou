package dev.zwander.compose.libmonet.dynamiccolor

import dev.zwander.compose.libmonet.utils.MathUtils.lerp


/**
 * A class containing a value that changes with the contrast level.
 *
 *
 * Usually represents the contrast requirements for a dynamic color on its background. The four
 * values correspond to values for contrast levels -1.0, 0.0, 0.5, and 1.0, respectively.
 */
class ContrastCurve
/**
 * Creates a `ContrastCurve` object.
 *
 * @param low Value for contrast level -1.0
 * @param normal Value for contrast level 0.0
 * @param medium Value for contrast level 0.5
 * @param high Value for contrast level 1.0
 */(
    /** Value for contrast level -1.0  */
    private val low: Double,
    /** Value for contrast level 0.0  */
    private val normal: Double,
    /** Value for contrast level 0.5  */
    private val medium: Double,
    /** Value for contrast level 1.0  */
    private val high: Double
) {
    /**
     * Returns the value at a given contrast level.
     *
     * @param contrastLevel The contrast level. 0.0 is the default (normal); -1.0 is the lowest; 1.0
     * is the highest.
     * @return The value. For contrast ratios, a number between 1.0 and 21.0.
     */
    fun get(contrastLevel: Double): Double {
        return if (contrastLevel <= -1.0) {
            low
        } else if (contrastLevel < 0.0) {
            lerp(this.low, this.normal, (contrastLevel - -1) / 1)
        } else if (contrastLevel < 0.5) {
            lerp(this.normal, this.medium, (contrastLevel - 0) / 0.5)
        } else if (contrastLevel < 1.0) {
            lerp(this.medium, this.high, (contrastLevel - 0.5) / 0.5)
        } else {
            high
        }
    }
}
