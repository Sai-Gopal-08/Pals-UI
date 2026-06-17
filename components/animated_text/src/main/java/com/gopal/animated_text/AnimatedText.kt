package com.gopal.animated_text

import android.icu.text.BreakIterator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.StringCharacterIterator
import kotlin.time.Duration.Companion.milliseconds

/**
 * Displays text with character-by-character animation effects.
 *
 * Each character appears sequentially with the specified animation style, creating engaging
 * text reveal effects. Supports multiple animation styles including scale, fade, jump, and rotation.
 *
 * The component uses [BreakIterator] to properly handle grapheme clusters, ensuring correct
 * animation of complex characters including emojis and multibyte Unicode characters.
 *
 * @param text The text content to animate. Each character (grapheme cluster) will animate individually.
 *             If empty, nothing is rendered.
 * @param spec Configuration for animation timing, style, and text appearance.
 * @param modifier Modifier to be applied to the FlowRow container.
 *
 * @see AnimatedTextSpec
 * @see AnimatedTextStyle
 * @see AnimatedTextDefaults
 */
@Composable
fun AnimatedText(
    text: String,
    spec: AnimatedTextSpec,
    modifier: Modifier = Modifier
) {
    AnimatedTextInternal(
        text = text,
        spec = spec,
        modifier = modifier
    )
}

/**
 * Internal implementation of animated text rendering.
 *
 * Handles the core logic for character parsing, visibility tracking, and animation orchestration.
 * Uses [BreakIterator] for proper grapheme cluster detection and [AnimatedVisibility] for
 * individual character animations.
 *
 * @param text The text to animate
 * @param spec Animation and styling configuration
 * @param modifier Modifier for the FlowRow container
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimatedTextInternal(
    text: String,
    spec: AnimatedTextSpec,
    modifier: Modifier = Modifier
) {
    if (text.isEmpty()) {
        return
    }

    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val animationDuration = spec.animationDurationForEachCharacter
    val enterTransition: EnterTransition = when (spec.animationStyle) {
        is AnimatedTextStyle.ScaleIn -> scaleIn(
            animationSpec = tween(animationDuration),
            initialScale = 0f
        )

        is AnimatedTextStyle.ScaleOut -> scaleIn(
            animationSpec = tween(animationDuration),
            initialScale = 1.4f
        )

        is AnimatedTextStyle.FadeIn -> fadeIn(animationSpec = tween(animationDuration))

        is AnimatedTextStyle.JumpIn -> {
            val animationTransitionStyle = spec.animationStyle.animationTransitionStyle
            val animationSpec: FiniteAnimationSpec<IntOffset> = when (animationTransitionStyle) {
                AnimatedTextTransitionStyle.SMOOTH -> tween(animationDuration)
                AnimatedTextTransitionStyle.SPRING -> spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            }
            slideInVertically(
                animationSpec = animationSpec,
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn(
                animationSpec = tween(animationDuration)
            )
        }

        else -> EnterTransition.None
    }

    // Store all characters immediately
    val allCharacters = remember(text) {
        breakIterator.text = StringCharacterIterator(text)
        val chars = mutableListOf<String>()
        var start = 0
        var end = breakIterator.next()
        while (end != BreakIterator.DONE) {
            chars.add(text.substring(start, end))
            start = end
            end = breakIterator.next()
        }
        chars
    }

    // Track which characters are visible
    var visibleCount by remember(text) { mutableIntStateOf(0) }

    LaunchedEffect(text) {
        visibleCount = 0
        delay(500.milliseconds)

        // Reveal characters one by one
        allCharacters.indices.forEach { index ->
            visibleCount = index + 1
            delay(spec.animationDelayForEachCharacter.milliseconds)
        }
    }

    FlowRow(modifier = modifier) {
        allCharacters.forEachIndexed { index, char ->
            key(index) {
                val isVisible = index < visibleCount

                when (spec.animationStyle) {
                    is AnimatedTextStyle.RotateIn -> {
                        AnimatedCharacterWithRotation(
                            char = char,
                            isVisible = isVisible,
                            textStyle = spec.textStyle,
                            animationDuration = animationDuration
                        )
                    }

                    else -> {
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = enterTransition
                        ) {
                            Text(text = char, style = spec.textStyle)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Renders a single character with rotation animation effect.
 *
 * This composable is used exclusively for [AnimatedTextStyle.RotateIn] animations.
 * It manages rotation state and animates characters from a 45° tilt to vertical (0°)
 * position using spring physics, combined with fade-in effects.
 *
 * The rotation happens around the bottom-center point, creating a "standing up" visual effect.
 *
 * @param char The character to render and animate
 * @param isVisible Whether the character should be visible (triggers animation)
 * @param textStyle Text styling to apply to the character
 * @param animationDuration Duration in milliseconds for fade animations
 */
@Composable
private fun AnimatedCharacterWithRotation(
    char: String,
    isVisible: Boolean,
    textStyle: TextStyle,
    animationDuration: Int
) {
    var rotationAngle by remember { mutableFloatStateOf(if (isVisible) 0f else 45f) }

    LaunchedEffect(isVisible) {
        rotationAngle = if (isVisible) 0f else 45f
    }

    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "rotation"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(animationDuration))
    ) {
        Text(
            text = char,
            style = textStyle,
            modifier = Modifier.graphicsLayer {
                rotationZ = animatedRotation
                transformOrigin = TransformOrigin(0.5f, 1f)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedTextPreview() {
    var resetKey by remember { mutableIntStateOf(0) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = { resetKey++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Animation")
            }

            key(resetKey) {
                AnimatedText(
                    text = "Hello, World! 👋🌍",
                    modifier = Modifier.fillMaxWidth(),
                    spec = AnimatedTextDefaults.Default.copy(
                        animationStyle = AnimatedTextStyle.RotateIn
                    )
                )
            }
        }
    }
}
