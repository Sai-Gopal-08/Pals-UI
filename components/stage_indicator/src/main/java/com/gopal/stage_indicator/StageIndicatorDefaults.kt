package com.gopal.stage_indicator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Default values for [StageIndicator].
 *
 * Non-theme values (spacing, height, border width) are plain singletons — allocated once.
 * Theme-dependent values are resolved inside [spec], a `@Composable` function that reads
 * [MaterialTheme.colorScheme] — following the same pattern as Material3's own defaults.
 */
object StageIndicatorDefaults {

    private const val TOTAL_PAGES = 3

    /** Default spacing between indicator dots. */
    val indicatorSpacing = 16.dp

    /** Default height of each indicator dot. */
    val indicatorHeight = 8.dp

    /** Default border width on each indicator dot — `1.dp` (subtle outline). */
    val borderWidth = 1.dp

    /**
     * Default [StageIndicatorSpec] derived from the current [MaterialTheme].
     *
     * - Filled dots use [MaterialTheme.colorScheme.primary].
     * - Unfilled dots use [MaterialTheme.colorScheme.surfaceVariant].
     * - Border uses [MaterialTheme.colorScheme.outline].
     */
    @Composable
    fun spec(): StageIndicatorSpec = StageIndicatorSpec(
        totalPages = TOTAL_PAGES,
        indicatorSpacing = indicatorSpacing,
        indicatorHeight = indicatorHeight,
        stageIndicatorColors = StageIndicatorColors(
            filledIndicatorColor = MaterialTheme.colorScheme.primary,
            unfilledIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        indicatorBorderSpec = StageIndicatorBorderSpec(
            borderWidth = borderWidth,
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        ),
    )

    /** Convenience for creating a [StageIndicatorColors] with solid unfilled color. */
    fun colors(
        filledColor: Color = Color.Unspecified,
        unfilledColor: Color = Color.Unspecified,
    ): StageIndicatorColors = StageIndicatorColors(
        filledIndicatorColor = filledColor,
        unfilledIndicatorColor = unfilledColor,
    )
}

