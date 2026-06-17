package com.gopal.animated_text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Provides default configurations for [AnimatedTextSpec].
 *
 * Pre-configured animation specifications that can be used directly
 * or as a starting point for customization via [AnimatedTextSpec.copy].
 *
 * @see AnimatedTextSpec
 */
object AnimatedTextDefaults {
    /**
     * Default text animation specification with balanced settings.
     *
     * Configuration:
     * - Animation duration: 300ms per character
     * - Delay between characters: 100ms
     * - Animation style: [AnimatedTextStyle.ScaleIn]
     * - Text style: 16sp semi-bold black text with 2sp letter spacing
     *
     * Example usage:
     * ```
     * AnimatedText(
     *     text = "Hello World",
     *     spec = AnimatedTextDefaults.Default
     * )
     *
     * // Customize via copy
     * AnimatedText(
     *     text = "Custom Animation",
     *     spec = AnimatedTextDefaults.Default.copy(
     *         animationStyle = AnimatedTextStyle.RotateIn
     *     )
     * )
     * ```
     */
    val Default: AnimatedTextSpec = AnimatedTextSpec(
        animationDurationForEachCharacter = 300,
        animationDelayForEachCharacter = 100,
        animationStyle = AnimatedTextStyle.ScaleIn,
        textStyle = TextStyle(
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            fontSize = 16.sp
        )
    )
}

