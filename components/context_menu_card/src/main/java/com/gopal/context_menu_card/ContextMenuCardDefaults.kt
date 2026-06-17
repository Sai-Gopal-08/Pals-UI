package com.gopal.context_menu_card

import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Default values and styling for [ContextMenuCard].
 *
 * This object provides sensible defaults for all customizable aspects of the card component.
 * These values are used when corresponding properties in [ContextMenuCardSpec] are null.
 */
object ContextMenuCardDefaults {
    /**
     * Default card shape with 16dp rounded corners.
     */
    val cardShape = RoundedCornerShape(16.dp)

    /**
     * Default card colors using Material3 theme colors.
     */
    val cardColors: CardColors
        @Composable
        get() = CardDefaults.cardColors()

    /**
     * Default animation specification for the hover/scale effect.
     * Uses a 250ms tween animation for smooth transitions.
     */
    val cardHoverAnimationSpec = tween<Float>(durationMillis = 250)

    /**
     * Elevation applied when the contextual menu is visible (hovered state).
     * Elevated to 8dp to visually lift the card above other content.
     */
    val hoveredCardElevation: CardElevation
        @Composable
        get() = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        )

    /**
     * Elevation applied when the card is in its default state (menu not visible).
     * Set to 2dp for subtle depth.
     */
    val defaultCardElevation: CardElevation
        @Composable
        get() = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
}

