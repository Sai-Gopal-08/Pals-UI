package com.gopal.circular_gauge_chart

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

/**
 * Default specs, color palettes, typography and constants for [CircularGaugeChart].
 *
 * [defaultSpec], [defaultColors] and [defaultTypography] are `@Composable` because
 * they read `MaterialTheme`, so the gauge integrates with the host app's theme by
 * default. For a fully static (non-themed) default, construct
 * [CircularGaugeChartSpec] directly.
 */
object CircularGaugeChartDefaults {

    private const val ANIMATION_DURATION_MS = 1200

    /**
     * Intrinsic size used when [CircularGaugeChart] is placed in an unbounded
     * parent (e.g. inside a vertical scroll without an explicit `Modifier.size`).
     * When the parent supplies bounded constraints in both axes, the gauge fills
     * those instead and this value is ignored.
     */
    val FallbackSize = 240.dp

    /** Theme-aware default spec. */
    @Composable
    @ReadOnlyComposable
    fun defaultSpec(): CircularGaugeChartSpec =
        CircularGaugeChartSpec(
            animationDurationMs = ANIMATION_DURATION_MS,
            strokeWidth = 20.dp,
            innerDiscInset = 24.dp,
            decimalPlaces = 2,
            colors = defaultColors(),
            typography = defaultTypography(),
        )

    /** Theme-aware default colors derived from [MaterialTheme.colorScheme]. */
    @Composable
    @ReadOnlyComposable
    fun defaultColors(): CircularGaugeChartColors {
        val scheme = MaterialTheme.colorScheme
        return CircularGaugeChartColors(
            backgroundCircle = scheme.surfaceVariant,
            innerDisc = scheme.surface,
            progress = scheme.primary,
            primaryLabel = scheme.onSurface,
            secondaryLabel = scheme.onSurfaceVariant,
        )
    }

    /** Theme-aware default typography derived from [MaterialTheme.typography]. */
    @Composable
    @ReadOnlyComposable
    fun defaultTypography(): CircularGaugeChartTypography {
        val typography = MaterialTheme.typography
        return CircularGaugeChartTypography(
            primaryLabel = typography.titleLarge,
            secondaryLabel = typography.bodyLarge,
            percentageLabel = typography.bodyLarge,
        )
    }
}

