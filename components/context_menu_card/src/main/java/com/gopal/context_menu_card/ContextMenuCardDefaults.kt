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
 * Static values ([cardShape], [cardHoverAnimationSpec]) are plain singletons.
 * Theme-dependent values ([cardColors], [hoveredCardElevation], [defaultCardElevation])
 * are exposed as `@Composable` functions — the same pattern used by Material3's own
 * [CardDefaults] — so they are only callable in composable scope and always reflect
 * the current theme without relying on property getters.
 */
object ContextMenuCardDefaults {

    /**
     * Default card shape with 16dp rounded corners.
     */
    val cardShape = RoundedCornerShape(16.dp)

    /**
     * Default animation specification for the hover/scale effect.
     * Uses a 250ms tween animation for smooth transitions.
     */
    val cardHoverAnimationSpec = tween<Float>(durationMillis = 250)

    /**
     * Default card colors resolved from the current Material3 theme.
     *
     * Exposed as a `@Composable` function (not a property getter) so it is only
     * callable in composable scope and is consistent with how [CardDefaults.cardColors]
     * itself is exposed. [CardColors] is `@Immutable` — equal instances across
     * recompositions do not cause unnecessary downstream recompositions.
     */
    @Composable
    fun cardColors(): CardColors = CardDefaults.cardColors()

    /**
     * Default elevation when the contextual menu is visible — 8dp.
     *
     * Exposed as a `@Composable` function (not a property getter) so it is only
     * callable in composable scope. [CardElevation] is `@Immutable` — equal instances
     * across recompositions do not cause unnecessary downstream recompositions.
     */
    @Composable
    fun hoveredCardElevation(): CardElevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 8.dp
    )

    /**
     * Default elevation when the card is in its resting state — 2dp.
     *
     * Exposed as a `@Composable` function (not a property getter) so it is only
     * callable in composable scope. [CardElevation] is `@Immutable` — equal instances
     * across recompositions do not cause unnecessary downstream recompositions.
     */
    @Composable
    fun defaultCardElevation(): CardElevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp
    )
}
