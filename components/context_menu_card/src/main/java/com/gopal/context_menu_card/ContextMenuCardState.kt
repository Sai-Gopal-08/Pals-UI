package com.gopal.context_menu_card

import androidx.compose.runtime.Stable

/**
 * State holder for [ContextMenuCard] controlling user interaction.
 *
 * This data class encapsulates the runtime state for enabling or disabling card interactions.
 * Menu visibility is managed internally by the component, so it's not part of this state.
 *
 * @property areClicksEnabled Whether tap and long-press gestures are currently enabled.
 * When false, the `onCardClick` and `onLongPress` callbacks will not be invoked.
 * Useful for temporarily disabling interaction during loading states or animations.
 * Defaults to true.
 */
@Stable
data class ContextMenuCardState(
    val areClicksEnabled: Boolean = true,
)

