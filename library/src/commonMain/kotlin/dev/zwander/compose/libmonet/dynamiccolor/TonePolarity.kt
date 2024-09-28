package dev.zwander.compose.libmonet.dynamiccolor

/**
 * Describes the relationship in lightness between two colors.
 *
 *
 * 'nearer' and 'farther' describes closeness to the surface roles. For instance,
 * ToneDeltaPair(A, B, 10, 'nearer', stayTogether) states that A should be 10 lighter than B in
 * light mode, and 10 darker than B in dark mode.
 *
 *
 * See `ToneDeltaPair` for details.
 */
enum class TonePolarity {
    DARKER,
    LIGHTER,
    NEARER,
    FARTHER
}
