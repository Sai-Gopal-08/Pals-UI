package com.gopal.animated_text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Configuration specification for [AnimatedText] appearance and animation behaviour.
 *
 * Defines all animation parameters including timing, style, and text appearance.
 * Each character in the text animates sequentially according to these specifications.
 *
 * @property animationDurationForEachCharacter Duration in milliseconds for each character's animation.
 *                                             Must be non-negative.
 * @property animationDelayForEachCharacter Delay in milliseconds between each character's animation start.
 *                                          Must be at least 50ms to ensure animation visibility.
 * @property animationStyle The type of animation effect applied to each character.
 * @property textStyle The text styling applied to all characters (color, font, size, etc.).
 *
 * @throws IllegalArgumentException if durations are negative or delay is less than 50ms.
 *
 * @see AnimatedTextStyle
 * @see AnimatedTextDefaults
 *
 * Example:
 * ```
 * AnimatedTextSpec(
 *     animationDurationForEachCharacter = 300,
 *     animationDelayForEachCharacter = 100,
 *     animationStyle = AnimatedTextStyle.ScaleIn,
 *     textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
 * )
 * ```
 */
@Immutable
data class AnimatedTextSpec(
    val animationDurationForEachCharacter: Int,
    val animationDelayForEachCharacter: Long,
    val animationStyle: AnimatedTextStyle,
    val textStyle: TextStyle
) {
    init {
        require(animationDurationForEachCharacter >= 0) { "Animation duration for each character must be non-negative." }
        require(animationDelayForEachCharacter >= 0) { "Animation delay for each character must be non-negative." }
        require(animationDelayForEachCharacter >= 50) { "Animation delay for each character should be at least 50 milliseconds to ensure visibility of the animation." }
    }
}

