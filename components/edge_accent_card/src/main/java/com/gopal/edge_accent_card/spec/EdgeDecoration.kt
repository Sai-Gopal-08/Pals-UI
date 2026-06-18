package com.gopal.edge_accent_card.spec

import androidx.compose.runtime.Immutable

/**
 * Defines the decoration applied to the edges of [com.gopal.edge_accent_card.EdgeAccentCard].
 *
 * @property edges List of edges to decorate. Each entry determines which side is drawn and
 *   whether corner arcs are included.
 * @property style Visual style applied to all edges — [EdgeStyle.Solid] or [EdgeStyle.Gradient].
 * @property span Optional fractional span for partial edge drawing. Defaults to `null` (full edge).
 *   **Ignored** for [Edge.Left.WithCorners] and [Edge.Right.WithCorners] — those always draw full height.
 */
@Immutable
data class EdgeDecoration(
    val edges: List<Edge>,
    val style: EdgeStyle,
    val span: EdgeSpan? = null,
)

