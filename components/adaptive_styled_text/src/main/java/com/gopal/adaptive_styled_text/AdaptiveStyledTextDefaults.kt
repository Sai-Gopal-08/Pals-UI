package com.gopal.adaptive_styled_text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * Defaults and convenience builders for [AdaptiveStyledText].
 *
 * The factory functions here build [AdaptiveStyledTextSegment] instances for common
 * inline styles (bold, italic, underline, strikethrough, highlighted) without forcing
 * callers to remember the exact [FontWeight] / [TextDecoration] enum values.
 *
 * For building the [AdaptiveStyledTextSpec] itself, prefer constructing it directly —
 * the data class constructor already exposes every parameter with sensible defaults.
 */
object AdaptiveStyledTextDefaults {

    /** Plain segment with no inline overrides. */
    fun segment(
        text: String,
        color: Color = Color.Unspecified,
    ): AdaptiveStyledTextSegment = AdaptiveStyledTextSegment(
        text = text,
        color = color,
    )

    /** Bold segment ([FontWeight.Bold]). */
    fun boldSegment(
        text: String,
        color: Color = Color.Unspecified,
    ): AdaptiveStyledTextSegment = AdaptiveStyledTextSegment(
        text = text,
        color = color,
        fontWeight = FontWeight.Bold,
    )

    /** Italic segment ([FontStyle.Italic]). */
    fun italicSegment(
        text: String,
        color: Color = Color.Unspecified,
    ): AdaptiveStyledTextSegment = AdaptiveStyledTextSegment(
        text = text,
        color = color,
        fontStyle = FontStyle.Italic,
    )

    /** Underlined segment ([TextDecoration.Underline]). */
    fun underlinedSegment(
        text: String,
        color: Color = Color.Unspecified,
    ): AdaptiveStyledTextSegment = AdaptiveStyledTextSegment(
        text = text,
        color = color,
        textDecoration = TextDecoration.Underline,
    )

    /** Strikethrough segment ([TextDecoration.LineThrough]). */
    fun strikethroughSegment(
        text: String,
        color: Color = Color.Unspecified,
    ): AdaptiveStyledTextSegment = AdaptiveStyledTextSegment(
        text = text,
        color = color,
        textDecoration = TextDecoration.LineThrough,
    )

    /** Highlighted segment — semi-bold with a caller-chosen [color]. */
    fun highlightedSegment(
        text: String,
        color: Color,
    ): AdaptiveStyledTextSegment = AdaptiveStyledTextSegment(
        text = text,
        color = color,
        fontWeight = FontWeight.SemiBold,
    )
}

