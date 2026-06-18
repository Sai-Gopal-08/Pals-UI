package com.gopal.edge_accent_card.spec

import androidx.compose.runtime.Immutable

/**
 * Defines the fractional start and end position of an edge decoration on [com.gopal.edge_accent_card.EdgeAccentCard].
 *
 * Both fractions are in the range `0f → 1f`, where `0f` is the leading edge and `1f` the trailing.
 *
 * **Note:** Only applies to:
 * - [Edge.Top] and [Edge.Bottom] — controls horizontal span
 * - [Edge.Left.WithoutCorners] and [Edge.Right.WithoutCorners] — controls vertical span
 *
 * Ignored for [Edge.Left.WithCorners] and [Edge.Right.WithCorners] — those always draw full height.
 *
 * Use [com.gopal.edge_accent_card.EdgeAccentCardDefaults.FullEdge], [com.gopal.edge_accent_card.EdgeAccentCardDefaults.CenteredHalfEdge], etc. for presets.
 */
@Immutable
data class EdgeSpan(
    val startFraction: Float,
    val endFraction: Float,
) {
    init {
        require(startFraction in 0f..1f) { "startFraction must be between 0f and 1f" }
        require(endFraction in 0f..1f) { "endFraction must be between 0f and 1f" }
        require(startFraction <= endFraction) { "startFraction must be <= endFraction" }
    }
}

