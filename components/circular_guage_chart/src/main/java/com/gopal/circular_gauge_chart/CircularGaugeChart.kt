package com.gopal.circular_gauge_chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A circular gauge that animates a stroked arc from 0% to the current
 * [nowValue] / [totalValue] ratio, with a centered "now / total", optional
 * [title] subtitle, and a percentage readout.
 *
 * **Sizing.** The gauge fills the space granted by [modifier] when [modifier]
 * supplies bounded constraints in both axes. When the parent is unbounded
 * (e.g. inside a vertical scroll with no `Modifier.size`), the gauge falls
 * back to [CircularGaugeChartDefaults.FallbackSize]. The radius is always
 * derived from `min(width, height) / 2`.
 *
 * **Accessibility.** The component sets a single `contentDescription` on the
 * outer container. Callers may pass a fully-localized string in
 * [contentDescription]; passing `null` uses an English default of the form
 * `"Progress: 75 of 150, 50.00%"`.
 *
 * @param nowValue Current value. Clamped into `0..totalValue` when drawn.
 * @param totalValue Maximum value. Must be `> 0`.
 * @param modifier Modifier controlling the gauge's size and placement.
 * @param title Optional subtitle rendered below the ratio. Pass an empty
 *   string to hide it.
 * @param contentDescription Screen-reader description for the whole gauge.
 *   Pass `null` to use the built-in English default.
 * @param spec Visual + animation configuration. Defaults to a theme-aware spec.
 */
@Composable
fun CircularGaugeChart(
    nowValue: Int,
    totalValue: Int,
    modifier: Modifier = Modifier,
    title: String = "",
    contentDescription: String? = null,
    spec: CircularGaugeChartSpec = CircularGaugeChartDefaults.defaultSpec(),
) {
    // Validation + percentage are computed together, once per distinct input,
    // so the require() checks do not re-run on every recomposition.
    val percentage = remember(nowValue, totalValue, spec.decimalPlaces) {
        require(totalValue > 0) { "CircularGaugeChart requires totalValue > 0, was $totalValue" }
        require(spec.decimalPlaces >= 0) {
            "CircularGaugeChart requires spec.decimalPlaces >= 0, was ${spec.decimalPlaces}"
        }
        (nowValue.coerceIn(0, totalValue).toFloat() / totalValue) * 100f
    }

    val percentageLabel = remember(percentage, spec.decimalPlaces) {
        "%.${spec.decimalPlaces}f%%".format(percentage)
    }

    val animatedPercentage = remember { Animatable(0f) }
    LaunchedEffect(percentage, spec.animationDurationMs) {
        animatedPercentage.animateTo(
            targetValue = percentage,
            animationSpec = tween(durationMillis = spec.animationDurationMs),
        )
    }

    val effectiveDescription = contentDescription
        ?: "Progress: $nowValue of $totalValue, $percentageLabel"

    // Sizing: `modifier.size(FallbackSize)` lets a caller-supplied size in
    // `modifier` win (it comes first in the chain and coerces the inner size),
    // while defaulting to FallbackSize when the caller gives no size. This also
    // guarantees a bounded measurement, so the gauge is safe inside unbounded
    // parents (e.g. vertical scroll) without measuring to infinity.
    Box(
        modifier = modifier
            .size(CircularGaugeChartDefaults.FallbackSize)
            .semantics { this.contentDescription = effectiveDescription },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val outerRadiusPx = size.minDimension / 2f
            val strokePx = spec.strokeWidth.toPx()
            val innerRadiusPx = (outerRadiusPx - spec.innerDiscInset.toPx()).coerceAtLeast(0f)
            val arcRadius = outerRadiusPx - strokePx / 2f

            // Background filled circle
            drawCircle(
                color = spec.colors.backgroundCircle,
                radius = outerRadiusPx,
                center = center,
            )

            // Progress arc
            drawArc(
                color = spec.colors.progress,
                startAngle = -90f,
                sweepAngle = (animatedPercentage.value / 100f) * 360f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
                size = Size(arcRadius * 2, arcRadius * 2),
                topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
            )

            // Inner disc to cut out the center
            drawCircle(
                color = spec.colors.innerDisc,
                radius = innerRadiusPx,
                center = center,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "$nowValue / $totalValue",
                style = spec.typography.primaryLabel.copy(color = spec.colors.primaryLabel),
            )
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = spec.typography.secondaryLabel.copy(color = spec.colors.secondaryLabel),
                )
            }
            Text(
                text = percentageLabel,
                style = spec.typography.percentageLabel.copy(color = spec.colors.progress),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularGaugeChartPreview() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularGaugeChart(
            nowValue = 75,
            totalValue = 150,
            title = "Progress",
        )
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 400)
@Composable
private fun CircularGaugeChartLargePreview() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularGaugeChart(
            nowValue = 75,
            totalValue = 150,
            title = "Progress",
            modifier = Modifier.size(360.dp),
        )
    }
}

