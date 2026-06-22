package com.gopal.adaptive_styled_text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * A single styled run of text inside an [AdaptiveStyledText].
 *
 * Each segment overrides only the style properties it specifies; unspecified properties
 * fall through to [AdaptiveStyledTextSpec.baseTextStyle].
 *
 * @property text The text content of this segment.
 * @property color Color applied to this segment. [Color.Unspecified] inherits from the base style.
 * @property fontWeight Optional weight override (e.g. [FontWeight.Bold]).
 * @property fontStyle Optional style override (e.g. [FontStyle.Italic]).
 * @property textDecoration Optional decoration override (e.g. [TextDecoration.Underline]).
 */
@Immutable
data class AdaptiveStyledTextSegment(
    val text: String,
    val color: Color = Color.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val textDecoration: TextDecoration? = null,
)

