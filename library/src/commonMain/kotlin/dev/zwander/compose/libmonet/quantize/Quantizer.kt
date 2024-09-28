package dev.zwander.compose.libmonet.quantize

internal interface Quantizer {
    fun quantize(pixels: IntArray?, maxColors: Int): QuantizerResult
}
