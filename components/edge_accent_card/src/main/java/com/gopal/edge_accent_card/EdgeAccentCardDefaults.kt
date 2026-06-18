package com.gopal.edge_accent_card

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gopal.edge_accent_card.spec.CardConfig
import com.gopal.edge_accent_card.spec.Edge
import com.gopal.edge_accent_card.spec.EdgeDecoration
import com.gopal.edge_accent_card.spec.EdgeSpan
import com.gopal.edge_accent_card.spec.EdgeStyle

/**
 * Default values and factory methods for [EdgeAccentCard].
 *
 * All non-theme values are plain singletons — allocated once, zero recomposition cost.
 * Theme-dependent values ([cardConfig]) are exposed as `@Composable` functions following
 * the same pattern as Material3's own defaults.
 */
@Suppress("unused")
object EdgeAccentCardDefaults {

    // ============================================
    // Scalar Defaults
    // ============================================

    /** Default corner radius for the card. */
    val DefaultCornerRadius: Dp = 16.dp

    /** Default stroke width for edge decorations. */
    val DefaultStrokeWidth: Dp = 4.dp

    // ============================================
    // EdgeSpan Presets
    // ============================================

    /** Full edge span — `0.0 → 1.0`. */
    val FullEdge: EdgeSpan = EdgeSpan(startFraction = 0f, endFraction = 1f)

    /** Centered half edge span — `0.25 → 0.75`. */
    val CenteredHalfEdge: EdgeSpan = EdgeSpan(startFraction = 0.25f, endFraction = 0.75f)

    /** Leading half edge span — `0.0 → 0.5`. */
    val LeadingHalfEdge: EdgeSpan = EdgeSpan(startFraction = 0f, endFraction = 0.5f)

    /** Trailing half edge span — `0.5 → 1.0`. */
    val TrailingHalfEdge: EdgeSpan = EdgeSpan(startFraction = 0.5f, endFraction = 1f)

    // ============================================
    // CardConfig
    // ============================================

    /**
     * Creates a default [com.gopal.edge_accent_card.spec.CardConfig] with Material3 theming.
     *
     * Must be called within a composable scope — reads [CardDefaults] which reads [MaterialTheme].
     */
    @Composable
    fun cardConfig(
        cornerRadius: Dp = DefaultCornerRadius,
        cardColors: CardColors = CardDefaults.cardColors(),
        cardBorder: androidx.compose.foundation.BorderStroke? = null,
        cardElevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ): CardConfig = CardConfig(
        cornerRadius = cornerRadius,
        cardColors = cardColors,
        cardBorder = cardBorder,
        cardElevation = cardElevation,
    )

    // ============================================
    // EdgeStyle Factories
    // ============================================

    /** Creates a solid [com.gopal.edge_accent_card.spec.EdgeStyle] with the given [color] and [strokeWidth]. */
    fun solidEdge(
        color: Color,
        strokeWidth: Dp = DefaultStrokeWidth,
    ): EdgeStyle.Solid = EdgeStyle.Solid(color = color, strokeWidth = strokeWidth)

    /**
     * Creates a gradient [EdgeStyle] that peaks at the center and fades toward both ends.
     *
     * @param centerAlpha Alpha at the center (peak intensity).
     * @param edgeAlpha Alpha at both ends (minimum intensity).
     */
    fun gradientEdge(
        color: Color,
        strokeWidth: Dp = DefaultStrokeWidth,
        centerAlpha: Float = 1f,
        edgeAlpha: Float = 0f,
    ): EdgeStyle.Gradient = EdgeStyle.Gradient(
        color = color,
        strokeWidth = strokeWidth,
        centerAlpha = centerAlpha,
        edgeAlpha = edgeAlpha,
    )

    // ============================================
    // Edge Factories
    // ============================================

    /** Left edge with corner arcs. [topSweepAngle] and [bottomSweepAngle] are in 0–90°. */
    fun leftEdgeWithCorners(
        topSweepAngle: Float = 60f,
        bottomSweepAngle: Float = 60f,
        arcStrokeWidthMultiplier: Float? = null,
    ): Edge.Left.WithCorners = Edge.Left.WithCorners(
        topSweepAngle = topSweepAngle,
        bottomSweepAngle = bottomSweepAngle,
        arcStrokeWidthMultiplier = arcStrokeWidthMultiplier,
    )

    /** Left edge without corner arcs. Respects [com.gopal.edge_accent_card.spec.EdgeDecoration.span]. */
    fun leftEdgeWithoutCorners(): Edge.Left.WithoutCorners = Edge.Left.WithoutCorners

    /** Right edge with corner arcs. [topSweepAngle] and [bottomSweepAngle] are in 0–90°. */
    fun rightEdgeWithCorners(
        topSweepAngle: Float = 60f,
        bottomSweepAngle: Float = 60f,
        arcStrokeWidthMultiplier: Float? = null,
    ): Edge.Right.WithCorners = Edge.Right.WithCorners(
        topSweepAngle = topSweepAngle,
        bottomSweepAngle = bottomSweepAngle,
        arcStrokeWidthMultiplier = arcStrokeWidthMultiplier,
    )

    /** Right edge without corner arcs. Respects [com.gopal.edge_accent_card.spec.EdgeDecoration.span]. */
    fun rightEdgeWithoutCorners(): Edge.Right.WithoutCorners = Edge.Right.WithoutCorners

    /** Top edge. Respects [com.gopal.edge_accent_card.spec.EdgeDecoration.span] (horizontal fraction). */
    fun topEdge(): Edge.Top = Edge.Top

    /** Bottom edge. Respects [com.gopal.edge_accent_card.spec.EdgeDecoration.span] (horizontal fraction). */
    fun bottomEdge(): Edge.Bottom = Edge.Bottom

    // ============================================
    // EdgeSpan Factory
    // ============================================

    /** Creates a custom [EdgeSpan] between [startFraction] and [endFraction] (`0f → 1f`). */
    fun edgeSpan(startFraction: Float, endFraction: Float): EdgeSpan =
        EdgeSpan(startFraction = startFraction, endFraction = endFraction)

    // ============================================
    // EdgeDecoration Factory
    // ============================================

    /** Creates an [com.gopal.edge_accent_card.spec.EdgeDecoration] with the given [edges], [style], and optional [span]. */
    fun edgeDecoration(
        edges: List<Edge>,
        style: EdgeStyle,
        span: EdgeSpan? = null,
    ): EdgeDecoration = EdgeDecoration(edges = edges, style = style, span = span)

    // ============================================
    // Common Presets
    // ============================================

    /** Left edge decoration with solid color. [includeCorners] toggles corner arc drawing. */
    fun leftEdgeDecoration(
        color: Color,
        strokeWidth: Dp = DefaultStrokeWidth,
        includeCorners: Boolean = true,
    ): EdgeDecoration = EdgeDecoration(
        edges = listOf(if (includeCorners) leftEdgeWithCorners() else leftEdgeWithoutCorners()),
        style = solidEdge(color, strokeWidth),
    )

    /** Right edge decoration with solid color. [includeCorners] toggles corner arc drawing. */
    fun rightEdgeDecoration(
        color: Color,
        strokeWidth: Dp = DefaultStrokeWidth,
        includeCorners: Boolean = true,
    ): EdgeDecoration = EdgeDecoration(
        edges = listOf(if (includeCorners) rightEdgeWithCorners() else rightEdgeWithoutCorners()),
        style = solidEdge(color, strokeWidth),
    )

    /** Top and bottom edges with a centered gradient. */
    fun horizontalEdgesDecoration(
        color: Color,
        strokeWidth: Dp = DefaultStrokeWidth,
        centerAlpha: Float = 1f,
        edgeAlpha: Float = 0f,
        span: EdgeSpan? = CenteredHalfEdge,
    ): EdgeDecoration = EdgeDecoration(
        edges = listOf(topEdge(), bottomEdge()),
        style = gradientEdge(color, strokeWidth, centerAlpha, edgeAlpha),
        span = span,
    )

    /** All four edges with a solid color. [includeCorners] toggles corner arc drawing. */
    fun allEdgesDecoration(
        color: Color,
        strokeWidth: Dp = DefaultStrokeWidth,
        includeCorners: Boolean = true,
    ): EdgeDecoration = EdgeDecoration(
        edges = listOf(
            if (includeCorners) leftEdgeWithCorners() else leftEdgeWithoutCorners(),
            if (includeCorners) rightEdgeWithCorners() else rightEdgeWithoutCorners(),
            topEdge(),
            bottomEdge(),
        ),
        style = solidEdge(color, strokeWidth),
    )
}

