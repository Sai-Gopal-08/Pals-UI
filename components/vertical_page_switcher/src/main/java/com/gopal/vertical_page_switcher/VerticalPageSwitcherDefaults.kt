package com.gopal.vertical_page_switcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defaults for [VerticalPageSwitcher].
 *
 * - [Default] — pre-built [VerticalPageSwitcherSpec] (no `@Composable` allocation cost).
 * - [NavButtons] — ready-made navigation slot rendering "Previous"/"Next" Material buttons,
 *   intended to be passed as the `navigation` parameter of [VerticalPageSwitcher].
 */
object VerticalPageSwitcherDefaults {

    /** Default animation duration in milliseconds. */
    const val DefaultAnimationDurationMillis: Int = 800

    /**
     * Singleton default spec. Plain `val` — allocated once, no per-recomposition cost.
     */
    val Default: VerticalPageSwitcherSpec = VerticalPageSwitcherSpec(
        animationDurationMillis = DefaultAnimationDurationMillis,
        enableLoop = false,
        startIndex = 0,
    )

    /**
     * Default navigation slot: a row of "Previous" and "Next" Material buttons.
     *
     * Pass this directly as the `navigation` parameter of [VerticalPageSwitcher], or wrap
     * it in your own composable to add extra controls around it.
     *
     * @param onPrev Click handler for the previous button.
     * @param onNext Click handler for the next button.
     * @param prevEnabled Whether the previous button is enabled.
     * @param nextEnabled Whether the next button is enabled.
     * @param prevLabel Text shown on the previous button.
     * @param nextLabel Text shown on the next button.
     * @param spacing Horizontal spacing between the two buttons.
     * @param prevColors Colors for the previous button.
     * @param nextColors Colors for the next button.
     * @param modifier Modifier applied to the surrounding [Row].
     */
    @Composable
    fun NavButtons(
        onPrev: () -> Unit,
        onNext: () -> Unit,
        prevEnabled: Boolean,
        nextEnabled: Boolean,
        modifier: Modifier = Modifier,
        prevLabel: String = "Previous",
        nextLabel: String = "Next",
        spacing: Dp = 16.dp,
        prevColors: ButtonColors = ButtonDefaults.buttonColors(),
        nextColors: ButtonColors = ButtonDefaults.buttonColors(),
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(spacing),
        ) {
            Button(onClick = onPrev, enabled = prevEnabled, colors = prevColors) {
                Text(prevLabel)
            }
            Button(onClick = onNext, enabled = nextEnabled, colors = nextColors) {
                Text(nextLabel)
            }
        }
    }
}

