package com.gopal.animated_text

/**
 * Defines the transition style for animations in [AnimatedText].
 *
 * This enum controls the animation physics and timing behavior when characters appear.
 *
 * @see AnimatedTextStyle.JumpIn
 */
enum class AnimatedTextTransitionStyle {
    /**
     * Smooth linear transition using tween animation.
     * Provides consistent, predictable motion without overshoot.
     */
    SMOOTH,

    /**
     * Spring-based transition with bounce effect.
     * Creates natural, physics-based motion with overshoot and settling.
     */
    SPRING
}

