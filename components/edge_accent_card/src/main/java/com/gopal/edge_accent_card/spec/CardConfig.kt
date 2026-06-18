package com.gopal.edge_accent_card.spec

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Visual configuration for the card container of [com.gopal.edge_accent_card.EdgeAccentCard].
 *
 * Controls shape, colors, border, and elevation. Edge decoration is configured separately
 * via [EdgeDecoration].
 */
@Immutable
data class CardConfig(
    val cornerRadius: Dp,
    val cardColors: CardColors,
    val cardBorder: BorderStroke? = null,
    val cardElevation: CardElevation,
)

