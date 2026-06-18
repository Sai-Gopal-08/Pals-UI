package com.gopal.styled_switch

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Default values for [StyledSwitch].
 *
 * Non-theme values (shape, size, elevation) are plain singletons — allocated once.
 * Theme-dependent values (colors) are exposed as `@Composable` functions that read
 * [MaterialTheme.colorScheme] — following the same pattern as Material3's own defaults.
 */
object StyledSwitchDefaults {

    private const val ANIMATION_DURATION_IN_MILLIS = 900

    /** Default thumb size. */
    val thumbSize = 32.dp

    /** Default shadow elevation applied to the thumb. */
    val elevation = 4.dp

    /** Default track shape — pill/circle. */
    val trackShape = CircleShape

    /** Default border spec — no visible border. */
    val borderSpec = StyledSwitchBorderSpec(
        borderColor = Color.Transparent,
        borderWidth = 0.dp,
    )

    /** Default icons — none. Override with [StyledSwitchIcons] to add thumb icons. */
    val icons: StyledSwitchIcons? = null

    /**
     * Default color set derived from the current [MaterialTheme].
     *
     * Returns a new [StyledSwitchColors] on each call, but [StyledSwitchColors] is `@Immutable`
     * so downstream composables skip recomposition when values are structurally equal.
     */
    @Composable
    fun colors(): StyledSwitchColors = StyledSwitchColors(
        checkedTrackColor = MaterialTheme.colorScheme.primary,
        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
        iconTint = MaterialTheme.colorScheme.onPrimary,
    )

    /**
     * Default full spec for [StyledSwitch].
     *
     * Combines all default values into a single [StyledSwitchSpec].
     * Must be called within a composable scope due to [colors].
     */
    @Composable
    fun spec(): StyledSwitchSpec = StyledSwitchSpec(
        animationDuration = ANIMATION_DURATION_IN_MILLIS,
        elevation = elevation,
        thumbSize = thumbSize,
        trackShape = trackShape,
        borderSpec = borderSpec,
        icons = icons,
        colors = colors(),
    )
}

