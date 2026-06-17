package com.gopal.animated_text

import androidx.compose.runtime.Stable

/**
 * Defines the animation style for character appearance in [AnimatedText].
 *
 * Each style determines how individual characters animate into view as they appear sequentially.
 * Styles can be combined with different transition types for varied visual effects.
 *
 * @see AnimatedTextSpec
 */
@Stable
sealed class AnimatedTextStyle {
    /**
     * Characters scale in from invisible (0%) to full size (100%).
     * Creates a "growing" effect where characters materialize from nothing.
     */
    object ScaleIn : AnimatedTextStyle()

    /**
     * Characters scale from oversized (140%) down to normal size (100%).
     * Creates a "settling" effect where characters shrink into place.
     */
    object ScaleOut : AnimatedTextStyle()

    /**
     * Characters fade in from transparent to opaque.
     * Creates a simple, subtle appearance effect without size changes.
     */
    object FadeIn : AnimatedTextStyle()

    /**
     * Characters jump in from below with optional spring physics.
     *
     * @property animationTransitionStyle Controls whether the jump uses smooth or spring-based motion.
     *                                     Spring creates a bouncy, settling effect.
     */
    data class JumpIn(val animationTransitionStyle: AnimatedTextTransitionStyle = AnimatedTextTransitionStyle.SMOOTH) :
        AnimatedTextStyle()

    /**
     * Characters rotate from a tilted angle (45°) to vertical (0°) while appearing.
     * Creates a "standing up" effect where characters settle into upright position.
     * Uses spring physics for natural rotation with bounce.
     */
    object RotateIn : AnimatedTextStyle()
}

