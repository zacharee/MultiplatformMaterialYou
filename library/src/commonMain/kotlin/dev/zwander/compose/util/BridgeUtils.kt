package dev.zwander.compose.util

import androidx.compose.ui.graphics.Color

fun Color.Companion.alpha(color: Int): Int {
    return (Color(color).alpha * 255).toInt()
}

fun Color.Companion.red(color: Int): Int {
    return (Color(color).red * 255).toInt()
}

fun Color.Companion.green(color: Int): Int {
    return (Color(color).green * 255).toInt()
}

fun Color.Companion.blue(color: Int): Int {
    return (Color(color).blue * 255).toInt()
}
