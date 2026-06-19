package com.gopal.label_badge

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/**
 * Immutable configuration for [LabelBadge].
 *
 * Use [LabelBadgeDefaults.spec] for a Material3-themed default.
 *
 * @property containerColor Background color of the badge.
 * @property textStyle Text style applied to the badge value.
 * @property shape Shape of the badge container.
 * @property paddingAroundTextInDp Padding between the text and the badge container edges.
 * @property style Rendering strategy — [LabelBadgeStyle.CUSTOM] or [LabelBadgeStyle.MATERIAL3_BADGE].
 */
@Immutable
data class LabelBadgeSpec(
    val containerColor: Color,
    val textStyle: TextStyle,
    val shape: Shape,
    val paddingAroundTextInDp: Dp,
    val style: LabelBadgeStyle = LabelBadgeStyle.CUSTOM,
)

