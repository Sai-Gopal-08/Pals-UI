package com.gopal.vertical_page_switcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Default signature for the [VerticalPageSwitcher] `navigation` slot.
 *
 * The slot receives click handlers and enable flags computed by the switcher so
 * callers can render any navigation UI (buttons, dots, swipe affordances, etc.)
 * without re-implementing the boundary / loop logic.
 */
typealias VerticalPageSwitcherNavigationSlot = @Composable (
    onPrev: () -> Unit,
    onNext: () -> Unit,
    prevEnabled: Boolean,
    nextEnabled: Boolean,
) -> Unit

/**
 * Vertically animated page switcher: shows one page at a time and slides between them
 * vertically (with a cross-fade) when the user advances or retreats.
 *
 * Navigation UI is fully delegated to the [navigation] slot. The default slot uses
 * [VerticalPageSwitcherDefaults.NavButtons].
 *
 * @param pages Composable lambdas to render. Must contain at least one entry.
 * @param modifier Modifier applied to the outer container.
 * @param spec Animation + behaviour configuration. See [VerticalPageSwitcherSpec].
 * @param onPageViewed Invoked once after the visible page index changes (including the initial value).
 * @param navigation Composable slot that renders navigation controls; receives click handlers
 *   and enable flags. Defaults to [VerticalPageSwitcherDefaults.NavButtons].
 */
@Composable
fun VerticalPageSwitcher(
    pages: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    spec: VerticalPageSwitcherSpec = VerticalPageSwitcherDefaults.Default,
    onPageViewed: (Int) -> Unit = {},
    navigation: VerticalPageSwitcherNavigationSlot = { onPrev, onNext, prevEnabled, nextEnabled ->
        VerticalPageSwitcherDefaults.NavButtons(
            onPrev = onPrev,
            onNext = onNext,
            prevEnabled = prevEnabled,
            nextEnabled = nextEnabled,
        )
    },
) {
    require(pages.isNotEmpty()) { "VerticalPageSwitcher requires at least one page" }

    val safeStartIndex = spec.startIndex.coerceIn(0, pages.lastIndex)
    var currentIndex by remember { mutableIntStateOf(safeStartIndex) }
    var direction by remember { mutableIntStateOf(1) } // 1 = next, -1 = previous

    val onNext = {
        direction = 1
        currentIndex = when {
            currentIndex < pages.lastIndex -> currentIndex + 1
            spec.enableLoop -> 0
            else -> currentIndex
        }
    }
    val onPrev = {
        direction = -1
        currentIndex = when {
            currentIndex > 0 -> currentIndex - 1
            spec.enableLoop -> pages.lastIndex
            else -> currentIndex
        }
    }

    LaunchedEffect(currentIndex) { onPageViewed(currentIndex) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        pages.forEachIndexed { index, page ->
            AnimatedVisibility(
                visible = index == currentIndex,
                enter = fadeIn(animationSpec = tween(spec.animationDurationMillis)) +
                    slideInVertically(
                        animationSpec = tween(spec.animationDurationMillis),
                        initialOffsetY = { if (direction > 0) it else -it },
                    ),
                exit = fadeOut(animationSpec = tween(spec.animationDurationMillis)) +
                    slideOutVertically(
                        animationSpec = tween(spec.animationDurationMillis),
                        targetOffsetY = { if (direction > 0) -it else it },
                    ),
            ) {
                page()
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            navigation(
                onPrev,
                onNext,
                spec.enableLoop || currentIndex > 0,
                spec.enableLoop || currentIndex < pages.lastIndex,
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun VerticalPageSwitcherDefaultNavPreview() {
    VerticalPageSwitcherPreviewContent()
}

@Preview(showBackground = true)
@Composable
private fun VerticalPageSwitcherCustomNavPreview() {
    VerticalPageSwitcherPreviewContent(
        navigation = { onPrev, onNext, prevEnabled, nextEnabled ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onPrev, enabled = prevEnabled) { Text("Prev") }
                Button(onClick = onNext, enabled = nextEnabled) { Text("Next") }
            }
        },
    )
}

@Composable
private fun VerticalPageSwitcherPreviewContent(
    navigation: VerticalPageSwitcherNavigationSlot? = null,
) {
    val pages = listOf<@Composable () -> Unit>(
        { CenteredText("Page 1") },
        { CenteredText("Page 2") },
        { CenteredText("Page 3") },
    )
    if (navigation == null) {
        VerticalPageSwitcher(pages = pages)
    } else {
        VerticalPageSwitcher(pages = pages, navigation = navigation)
    }
}

@Composable
private fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text)
    }
}

