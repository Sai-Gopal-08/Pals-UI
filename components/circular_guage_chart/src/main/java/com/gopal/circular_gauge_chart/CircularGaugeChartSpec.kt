package com.gopal.circular_gauge_chart

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/**
 * Visual and animation configuration for [CircularGaugeChart].
 *
 * Sizing is driven by the modifier passed to [CircularGaugeChart] - the gauge
 * fills the available space and derives its radius from it. Only the stroke
 * thickness and the inner-disc inset are configured here.
 *
 * @property animationDurationMs Duration of the sweep animation when the progress value changes.
 * @property strokeWidth Thickness of the progress arc.
 * @property innerDiscInset Distance from the outer edge to the inner filled disc.
 *   Set to `0.dp` to draw only the ring.
 * @property decimalPlaces Number of digits after the decimal point in the
 *   percentage label. `0` renders an integer (`"50%"`); `2` renders `"49.95%"`.
 *   Must be `>= 0`.
 * @property colors Color palette for the gauge.
 * @property typography Text styles for the three center labels.
 */
@Immutable
data class CircularGaugeChartSpec(
    val animationDurationMs: Int,
    val strokeWidth: Dp,
    val innerDiscInset: Dp,
    val decimalPlaces: Int,
    val colors: CircularGaugeChartColors,
    val typography: CircularGaugeChartTypography,
)

/**
 * Color palette for [CircularGaugeChart].
 *
 * @property backgroundCircle Filled circle drawn behind the progress arc.
 * @property innerDisc Filled disc drawn on top, leaving the arc/ring visible.
 * @property progress Stroke color of the progress arc and the percentage label.
 * @property primaryLabel Color of the primary "now / total" label.
 * @property secondaryLabel Color of the secondary title label.
 */
@Immutable
data class CircularGaugeChartColors(
    val backgroundCircle: Color,
    val innerDisc: Color,
    val progress: Color,
    val primaryLabel: Color,
    val secondaryLabel: Color,
)

/**
 * Typography for [CircularGaugeChart]'s three center labels.
 *
 * @property primaryLabel Style for the "now / total" line.
 * @property secondaryLabel Style for the optional title subtitle.
 * @property percentageLabel Style for the "NN%" readout.
 */
@Immutable
data class CircularGaugeChartTypography(
    val primaryLabel: TextStyle,
    val secondaryLabel: TextStyle,
    val percentageLabel: TextStyle,
)

