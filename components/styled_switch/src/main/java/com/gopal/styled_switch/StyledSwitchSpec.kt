package com.gopal.styled_switch

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.gopal.flexible_icon.FlexibleIconSource

/**
 * Configuration for [StyledSwitch].
 *
 * All properties are immutable — pass a new spec to change appearance.
 * Use [StyledSwitchDefaults.spec] for sensible defaults.
 */
@Immutable
data class StyledSwitchSpec(
    val animationDuration: Int,
    val elevation: Dp,
    val thumbSize: Dp,
    val trackShape: Shape,
    val borderSpec: StyledSwitchBorderSpec,
    val icons: StyledSwitchIcons?,
    val colors: StyledSwitchColors,
)

/**
 * Border configuration applied to the thumb of [StyledSwitch].
 */
@Immutable
data class StyledSwitchBorderSpec(
    val borderColor: Color,
    val borderWidth: Dp,
)

/**
 * Optional icons displayed inside the thumb of [StyledSwitch].
 *
 * Both icons are [FlexibleIconSource] — supporting vectors, drawable resources,
 * bitmap resources, or raw bitmaps. The correct icon is selected based on the checked state.
 */
@Immutable
data class StyledSwitchIcons(
    val checkedIcon: FlexibleIconSource,
    val uncheckedIcon: FlexibleIconSource,
)

/**
 * Color configuration for the track, thumb, and icon of [StyledSwitch].
 */
@Immutable
data class StyledSwitchColors(
    val checkedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedTrackColor: Color,
    val uncheckedThumbColor: Color,
    val iconTint: Color,
)

