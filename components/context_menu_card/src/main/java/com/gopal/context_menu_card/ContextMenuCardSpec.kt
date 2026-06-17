package com.gopal.context_menu_card

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape

/**
 * Specification for customizing the appearance and behavior of [ContextMenuCard].
 *
 * This immutable data class defines all visual and behavioral aspects of the card component,
 * from basic styling (shape, colors) to advanced features (animations, elevation changes).
 * Properties set to null will use defaults from [ContextMenuCardDefaults].
 *
 * @property uniqueKeySpecificToCard A unique, non-blank identifier for this card instance.
 * Used as the key for the pointer input modifier to properly scope gesture detection.
 * This ensures gesture handlers are correctly reset when the card's identity changes.
 * @property cardShape The shape of the card (e.g., [androidx.compose.foundation.shape.RoundedCornerShape]). Defaults to [ContextMenuCardDefaults.cardShape].
 * @property cardColors The color scheme for the card's container and content. Defaults to [ContextMenuCardDefaults.cardColors].
 * @property cardHoverAnimationSpec Animation specification for the scale transition when the menu appears.
 * Defaults to [ContextMenuCardDefaults.cardHoverAnimationSpec].
 * @property hoveredCardElevation Elevation applied to the card when the contextual menu is visible.
 * Defaults to [ContextMenuCardDefaults.hoveredCardElevation].
 * @property defaultCardElevation Elevation applied to the card when the contextual menu is not visible.
 * Defaults to [ContextMenuCardDefaults.defaultCardElevation].
 * @property cardBorderStroke Optional border stroke for the card. No border is applied by default.
 *
 * @throws IllegalArgumentException if [uniqueKeySpecificToCard] is blank.
 */
@Immutable
data class ContextMenuCardSpec(
    val uniqueKeySpecificToCard: String,
    val cardShape: Shape? = null,
    val cardColors: CardColors? = null,
    val cardHoverAnimationSpec: AnimationSpec<Float>? = null,
    val hoveredCardElevation: CardElevation? = null,
    val defaultCardElevation: CardElevation? = null,
    val cardBorderStroke: BorderStroke? = null
) {
    init {
        require(uniqueKeySpecificToCard.isNotBlank()) {
            "uniqueKeySpecificToCard must be a non-blank string to ensure that the pointer input modifier is correctly scoped to this card."
        }
    }
}

