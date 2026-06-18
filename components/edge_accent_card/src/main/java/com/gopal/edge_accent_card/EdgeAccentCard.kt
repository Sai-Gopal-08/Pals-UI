package com.gopal.edge_accent_card

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import com.gopal.edge_accent_card.spec.CardConfig
import com.gopal.edge_accent_card.spec.Edge
import com.gopal.edge_accent_card.spec.EdgeDecoration
import com.gopal.edge_accent_card.spec.EdgeStyle

/**
 * A Material3 card with customisable per-edge accent decorations.
 *
 * Edges can be styled with solid colors or symmetric gradients, with optional corner arcs
 * for left/right edges. Partial edge spans are supported via [com.gopal.edge_accent_card.spec.EdgeDecoration.span].
 *
 * @param cardConfig Visual configuration for the card container — shape, colors, border, elevation.
 * @param edgeDecoration Defines which edges to decorate and how they are styled.
 * @param modifier Modifier applied to the card.
 * @param cardContent Slot for the card content.
 *
 * @see EdgeAccentCardDefaults
 */
@Composable
fun EdgeAccentCard(
    cardConfig: CardConfig,
    edgeDecoration: EdgeDecoration,
    modifier: Modifier = Modifier,
    cardContent: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.applyEdgeStyling(cardConfig.cornerRadius, edgeDecoration),
        shape = RoundedCornerShape(cardConfig.cornerRadius),
        colors = cardConfig.cardColors,
        border = cardConfig.cardBorder,
        elevation = cardConfig.cardElevation,
    ) {
        cardContent()
    }
}

private fun Modifier.applyEdgeStyling(
    cornerRadius: Dp,
    edgeDecoration: EdgeDecoration,
) = this.drawWithCache {
    val stroke = edgeDecoration.style.strokeWidth.toPx()
    val radius = cornerRadius.toPx()
    val span = edgeDecoration.span ?: EdgeAccentCardDefaults.FullEdge
    val startFraction = span.startFraction
    val endFraction = span.endFraction

    val clipPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = size.toRect(),
                cornerRadius = CornerRadius(radius, radius),
            )
        )
    }

    onDrawWithContent {
        drawContent()
        clipPath(clipPath) {
            edgeDecoration.edges.forEach { edge ->
                when (edge) {
                    is Edge.Left.WithCorners -> drawLeftEdgeWithCorners(
                        edge = edge,
                        style = edgeDecoration.style,
                        stroke = stroke,
                        radius = radius,
                    )
                    is Edge.Left.WithoutCorners -> drawLeftEdgeWithoutCorners(
                        style = edgeDecoration.style,
                        stroke = stroke,
                        startFraction = startFraction,
                        endFraction = endFraction,
                    )
                    is Edge.Right.WithCorners -> drawRightEdgeWithCorners(
                        edge = edge,
                        style = edgeDecoration.style,
                        stroke = stroke,
                        radius = radius,
                    )
                    is Edge.Right.WithoutCorners -> drawRightEdgeWithoutCorners(
                        style = edgeDecoration.style,
                        stroke = stroke,
                        startFraction = startFraction,
                        endFraction = endFraction,
                    )
                    is Edge.Top -> drawTopEdge(
                        style = edgeDecoration.style,
                        stroke = stroke,
                        startFraction = startFraction,
                        endFraction = endFraction,
                    )
                    is Edge.Bottom -> drawBottomEdge(
                        style = edgeDecoration.style,
                        stroke = stroke,
                        startFraction = startFraction,
                        endFraction = endFraction,
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawLeftEdgeWithCorners(
    edge: Edge.Left.WithCorners,
    style: EdgeStyle,
    stroke: Float,
    radius: Float,
) {
    val lineStroke = stroke * 1.6f
    val arcStrokeWidth = stroke * (edge.arcStrokeWidthMultiplier ?: 1.4f)
    val startY = 0f
    val endY = size.height

    val (color, brush, arcColor) = when (style) {
        is EdgeStyle.Solid -> Triple(style.color, null, style.color)
        is EdgeStyle.Gradient -> {
            val gradBrush = Brush.verticalGradient(
                colorStops = buildSymmetricGradientStops(style.color, style.centerAlpha, style.edgeAlpha),
                startY = 0f, endY = size.height,
            )
            Triple(style.color, gradBrush, style.color.copy(alpha = style.centerAlpha))
        }
    }

    if (startY <= radius * 2) {
        drawArc(color = arcColor, startAngle = 180f, sweepAngle = edge.topSweepAngle,
            useCenter = false, topLeft = Offset(0f, 0f), size = Size(radius * 2, radius * 2),
            style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round))
    }
    if (brush != null) {
        drawLine(brush = brush, start = Offset(0f, startY), end = Offset(0f, endY),
            strokeWidth = lineStroke, cap = StrokeCap.Butt)
    } else {
        drawLine(color = color, start = Offset(0f, startY), end = Offset(0f, endY),
            strokeWidth = lineStroke, cap = StrokeCap.Butt)
    }
    if (endY >= size.height - radius * 2) {
        drawArc(color = arcColor, startAngle = 180f, sweepAngle = -edge.bottomSweepAngle,
            useCenter = false, topLeft = Offset(0f, size.height - radius * 2),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawLeftEdgeWithoutCorners(
    style: EdgeStyle,
    stroke: Float,
    startFraction: Float,
    endFraction: Float,
) {
    val startY = size.height * startFraction
    val endY = size.height * endFraction
    val (color, brush) = resolveColorAndBrush(style, startY, endY, horizontal = false)
    if (brush != null) {
        drawLine(brush = brush, start = Offset(0f, startY), end = Offset(0f, endY),
            strokeWidth = stroke, cap = StrokeCap.Round)
    } else {
        drawLine(color = color, start = Offset(0f, startY), end = Offset(0f, endY),
            strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

private fun DrawScope.drawRightEdgeWithCorners(
    edge: Edge.Right.WithCorners,
    style: EdgeStyle,
    stroke: Float,
    radius: Float,
) {
    val lineStroke = stroke * 1.6f
    val arcStrokeWidth = stroke * (edge.arcStrokeWidthMultiplier ?: 1.4f)
    val startY = 0f
    val endY = size.height

    val (color, brush, arcColor) = when (style) {
        is EdgeStyle.Solid -> Triple(style.color, null, style.color)
        is EdgeStyle.Gradient -> {
            val gradBrush = Brush.verticalGradient(
                colorStops = buildSymmetricGradientStops(style.color, style.centerAlpha, style.edgeAlpha),
                startY = 0f, endY = size.height,
            )
            Triple(style.color, gradBrush, style.color.copy(alpha = style.centerAlpha))
        }
    }

    if (startY <= radius * 2) {
        drawArc(color = arcColor, startAngle = 0f, sweepAngle = -edge.topSweepAngle,
            useCenter = false, topLeft = Offset(size.width - radius * 2, 0f),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round))
    }
    val lineStartY = maxOf(startY, radius)
    val lineEndY = minOf(endY, size.height - radius)
    if (lineStartY < lineEndY) {
        if (brush != null) {
            drawLine(brush = brush, start = Offset(size.width, lineStartY),
                end = Offset(size.width, lineEndY), strokeWidth = lineStroke, cap = StrokeCap.Butt)
        } else {
            drawLine(color = color, start = Offset(size.width, lineStartY),
                end = Offset(size.width, lineEndY), strokeWidth = lineStroke, cap = StrokeCap.Butt)
        }
    }
    if (endY >= size.height - radius * 2) {
        drawArc(color = arcColor, startAngle = 0f, sweepAngle = edge.bottomSweepAngle,
            useCenter = false, topLeft = Offset(size.width - radius * 2, size.height - radius * 2),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawRightEdgeWithoutCorners(
    style: EdgeStyle,
    stroke: Float,
    startFraction: Float,
    endFraction: Float,
) {
    val startY = size.height * startFraction
    val endY = size.height * endFraction
    val (color, brush) = resolveColorAndBrush(style, startY, endY, horizontal = false)
    if (brush != null) {
        drawLine(brush = brush, start = Offset(size.width, startY), end = Offset(size.width, endY),
            strokeWidth = stroke, cap = StrokeCap.Round)
    } else {
        drawLine(color = color, start = Offset(size.width, startY), end = Offset(size.width, endY),
            strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

private fun DrawScope.drawTopEdge(
    style: EdgeStyle,
    stroke: Float,
    startFraction: Float,
    endFraction: Float,
) {
    val startX = size.width * startFraction
    val endX = size.width * endFraction
    val (color, brush) = resolveColorAndBrush(style, startX, endX, horizontal = true)
    if (brush != null) {
        drawLine(brush = brush, start = Offset(startX, 0f), end = Offset(endX, 0f),
            strokeWidth = stroke, cap = StrokeCap.Round)
    } else {
        drawLine(color = color, start = Offset(startX, 0f), end = Offset(endX, 0f),
            strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

private fun DrawScope.drawBottomEdge(
    style: EdgeStyle,
    stroke: Float,
    startFraction: Float,
    endFraction: Float,
) {
    val startX = size.width * startFraction
    val endX = size.width * endFraction
    val (color, brush) = resolveColorAndBrush(style, startX, endX, horizontal = true)
    if (brush != null) {
        drawLine(brush = brush, start = Offset(startX, size.height), end = Offset(endX, size.height),
            strokeWidth = stroke, cap = StrokeCap.Round)
    } else {
        drawLine(color = color, start = Offset(startX, size.height), end = Offset(endX, size.height),
            strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

/** Resolves [EdgeStyle] to a flat [Color] or [Brush] for line drawing. */
private fun resolveColorAndBrush(
    style: EdgeStyle,
    start: Float,
    end: Float,
    horizontal: Boolean,
): Pair<Color, Brush?> = when (style) {
    is EdgeStyle.Solid -> Pair(style.color, null)
    is EdgeStyle.Gradient -> {
        val stops = buildSymmetricGradientStops(style.color, style.centerAlpha, style.edgeAlpha)
        val brush = if (horizontal) {
            Brush.horizontalGradient(colorStops = stops, startX = start, endX = end)
        } else {
            Brush.verticalGradient(colorStops = stops, startY = start, endY = end)
        }
        Pair(style.color, brush)
    }
}

/**
 * Builds a 21-point symmetric gradient — peaks at center (0.5) and fades to both edges.
 */
private fun buildSymmetricGradientStops(
    color: Color,
    peakAlpha: Float,
    edgeAlpha: Float = 0.01f,
): Array<Pair<Float, Color>> {
    val step = 0.05f
    val count = (1f / step).toInt()
    return (0..count).map { i ->
        val t = i * step
        val centerWeight = if (t <= 0.5f) t / 0.5f else (1f - t) / 0.5f
        val alpha = edgeAlpha + (peakAlpha - edgeAlpha) * centerWeight
        t to color.copy(alpha = alpha.coerceIn(0f, 1f))
    }.toTypedArray()
}

