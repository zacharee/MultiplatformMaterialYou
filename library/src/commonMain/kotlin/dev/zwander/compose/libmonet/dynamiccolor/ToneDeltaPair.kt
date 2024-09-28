package dev.zwander.compose.libmonet.dynamiccolor


/**
 * Documents a constraint between two DynamicColors, in which their tones must have a certain
 * distance from each other.
 *
 *
 * Prefer a DynamicColor with a background, this is for special cases when designers want tonal
 * distance, literally contrast, between two colors that don't have a background / foreground
 * relationship or a contrast guarantee.
 */
class ToneDeltaPair(
    /** The first role in a pair.  */
    val roleA: DynamicColor,
    /** The second role in a pair.  */
    val roleB: DynamicColor,
    /** Required difference between tones. Absolute value, negative values have undefined behavior.  */
    val delta: Double,
    /** The relative relation between tones of roleA and roleB, as described above.  */
    private val polarity: TonePolarity,
    /**
     * Whether these two roles should stay on the same side of the "awkward zone" (T50-59). This is
     * necessary for certain cases where one role has two backgrounds.
     */
    val stayTogether: Boolean
) {

    fun getPolarity(): TonePolarity {
        return polarity
    }
}
