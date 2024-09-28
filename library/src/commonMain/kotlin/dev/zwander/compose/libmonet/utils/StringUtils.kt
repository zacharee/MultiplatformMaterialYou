package dev.zwander.compose.libmonet.utils

import dev.zwander.compose.libmonet.utils.ColorUtils.blueFromArgb
import dev.zwander.compose.libmonet.utils.ColorUtils.greenFromArgb
import dev.zwander.compose.libmonet.utils.ColorUtils.redFromArgb
import korlibs.util.format

/** Utility methods for string representations of colors.  */
internal object StringUtils {
    /**
     * Hex string representing color, ex. #ff0000 for red.
     *
     * @param argb ARGB representation of a color.
     */
    fun hexFromArgb(argb: Int): String {
        val red = redFromArgb(argb)
        val blue = blueFromArgb(argb)
        val green = greenFromArgb(argb)
        return "#%02x%02x%02x".format(red, green, blue)
    }
}
