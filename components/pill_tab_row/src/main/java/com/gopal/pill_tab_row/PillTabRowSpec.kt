package com.gopal.pill_tab_row

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Immutable configuration for [PillTabRow].
 *
 * Use [PillTabRowDefaults.defaultPillTabRowSpec] for a pre-built default.
 *
 * @property pillHeight Height of the pill tab row container.
 * @property pillCornerRadius Corner radius of the overall pill shape and individual tab shapes.
 * @property pillTitleFontSize Font size for tab title labels.
 * @property colors Color configuration for background, indicator, and text.
 * @property borderSpec Border applied to each tab segment.
 * @property indicatorAnimationSpec Animation spec for the sliding indicator.
 * @property titles Tab label strings. Must contain at least 2 entries.
 */
@Immutable
data class PillTabRowSpec(
    val pillHeight: Dp,
    val pillCornerRadius: Dp,
    val pillTitleFontSize: TextUnit,
    val colors: PillTabRowColors,
    val borderSpec: PillBorderSpec,
    val indicatorAnimationSpec: AnimationSpec<Dp>,
    val titles: List<String>,
)

/**
 * Color configuration for [PillTabRow].
 */
@Immutable
data class PillTabRowColors(
    val backgroundColor: Color,
    val indicatorColor: Color,
    val textColor: Color,
    val selectedTextColor: Color,
)

/**
 * Border configuration applied to each tab segment of [PillTabRow].
 */
@Immutable
data class PillBorderSpec(
    val borderColor: Color,
    val borderWidth: Dp,
)