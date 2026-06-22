package com.gopal.adaptive_styled_text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Immutable configuration for [AdaptiveStyledText].
 *
 * Holds the list of styled segments together with the text-layout controls
 * (overflow, line limits, alignment) usually exposed by Material's `Text`.
 *
 * Use [AdaptiveStyledTextDefaults.spec] for a pre-built default.
 *
 * @property segments Styled segments rendered in order. Must not be empty.
 * @property baseTextStyle Base style applied to every segment; individual segments override
 *   only the properties they specify.
 * @property overflow How visual overflow is handled.
 * @property softWrap Whether the text should break at soft line breaks.
 * @property maxLines Maximum number of lines for the text to span.
 * @property minLines Minimum number of visible lines.
 * @property textAlign Alignment within the container; `null` means natural alignment.
 */
@Immutable
data class AdaptiveStyledTextSpec(
    val segments: List<AdaptiveStyledTextSegment>,
    val baseTextStyle: TextStyle = TextStyle.Default,
    val overflow: TextOverflow = TextOverflow.Clip,
    val softWrap: Boolean = true,
    val maxLines: Int = Int.MAX_VALUE,
    val minLines: Int = 1,
    val textAlign: TextAlign? = null,
)

