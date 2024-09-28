package dev.zwander.compose.libmonet.quantize

/** Represents result of a quantizer run  */
class QuantizerResult internal constructor(val colorToCount: Map<Int, Int>)
