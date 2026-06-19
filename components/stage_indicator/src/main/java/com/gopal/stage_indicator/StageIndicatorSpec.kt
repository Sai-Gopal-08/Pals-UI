package com.gopal.stage_indicator

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Immutable configuration for [StageIndicator].
 *
 * Use [StageIndicatorDefaults.spec] for a Material3-themed default.
 *
 * @property totalPages Total number of indicator dots to render.
 * @property indicatorSpacing Spacing between each indicator dot.
 * @property indicatorHeight Height (and effective size) of each indicator dot.
 * @property stageIndicatorColors Fill and unfill colors for the indicator dots.
 * @property indicatorBorderSpec Border applied to each indicator dot.
 */
@Immutable
data class StageIndicatorSpec(
    val totalPages: Int,
    val indicatorSpacing: Dp,
    val indicatorHeight: Dp,
    val stageIndicatorColors: StageIndicatorColors,
    val indicatorBorderSpec: StageIndicatorBorderSpec,
)

/**
 * Color configuration for [StageIndicator] dots.
 *
 * @property filledIndicatorColor Color for dots at or before [StageIndicator.currentIndex].
 * @property unfilledIndicatorColor Color for dots after [StageIndicator.currentIndex].
 */
@Immutable
data class StageIndicatorColors(
    val filledIndicatorColor: Color,
    val unfilledIndicatorColor: Color,
)

/**
 * Border configuration applied to each indicator dot of [StageIndicator].
 */
@Immutable
data class StageIndicatorBorderSpec(
    val borderWidth: Dp,
    val borderColor: Color,
)

