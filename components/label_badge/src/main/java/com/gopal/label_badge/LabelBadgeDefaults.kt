package com.gopal.label_badge

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Default values for [LabelBadge].
 *
 * Non-theme values (shape, padding) are plain singletons — allocated once.
 * Theme-dependent values are read inside [spec] which is a `@Composable` function,
 * following the same pattern as Material3's own defaults.
 */
object LabelBadgeDefaults {

    /** Default badge shape — 8dp rounded corners. */
    val shape = RoundedCornerShape(8.dp)

    /** Default padding between text and badge container edges. */
    val padding = 4.dp

    /**
     * Default [LabelBadgeSpec] derived from the current [MaterialTheme].
     *
     * Uses colors from [MaterialTheme.colorScheme] for container color and
     * text color.
     */
    @Composable
    fun spec(): LabelBadgeSpec = LabelBadgeSpec(
        containerColor = MaterialTheme.colorScheme.primary,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
        ),
        shape = shape,
        paddingAroundTextInDp = padding,
    )
}

