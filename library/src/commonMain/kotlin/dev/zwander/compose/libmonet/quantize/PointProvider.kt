package dev.zwander.compose.libmonet.quantize

/** An interface to allow use of different color spaces by quantizers.  */
interface PointProvider {
    /** The four components in the color space of an sRGB color.  */
    fun fromInt(argb: Int): DoubleArray

    /** The ARGB (i.e. hex code) representation of this color.  */
    fun toInt(point: DoubleArray): Int

    /**
     * Squared distance between two colors. Distance is defined by scientific color spaces and
     * referred to as delta E.
     */
    fun distance(a: DoubleArray, b: DoubleArray): Double
}
