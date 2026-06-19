package com.gopal.label_badge

import androidx.compose.runtime.Stable

/**
 * Determines the rendering strategy used by [LabelBadge].
 *
 * - [CUSTOM] — draws a sized `Box` with precise dimensions derived from text measurement.
 * - [MATERIAL3_BADGE] — delegates to Material3's `Badge` composable for standard badge styling.
 *
 * Marked `@Stable` so the Compose compiler can verify stability when used as a spec field.
 */
@Stable
enum class LabelBadgeStyle {
    CUSTOM,
    MATERIAL3_BADGE,
}

