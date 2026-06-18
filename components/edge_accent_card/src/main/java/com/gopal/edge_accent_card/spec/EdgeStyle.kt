package com.gopal.edge_accent_card.spec

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Defines the visual style of an edge decoration on [com.gopal.edge_accent_card.EdgeAccentCard].
 *
 * Marked `@Stable` so the Compose compiler can verify stability when used as a parameter,
 * preventing unnecessary recompositions when the style is structurally equal.
 */
@Stable
sealed class EdgeStyle {

    abstract val strokeWidth: Dp

    /** A single solid color edge. */
    data class Solid(
        val color: Color,
        override val strokeWidth: Dp,
    ) : EdgeStyle()

    /**
     * A symmetrically fading gradient edge.
     *
     * The gradient peaks at the center with [centerAlpha] and fades to [edgeAlpha] at both ends.
     */
    data class Gradient(
        val color: Color,
        override val strokeWidth: Dp,
        val centerAlpha: Float = 1f,
        val edgeAlpha: Float = 0f,
    ) : EdgeStyle()
}

