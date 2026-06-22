package com.gopal.vertical_page_switcher

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
 * Edges of [VerticalPageSwitcher_AnimatedContent]'s page range.
 *
 * Reported via [VerticalPageSwitcher_AnimatedContent]'s `onBoundaryReached` callback when the visible
 * page is the first ([Start]) or last ([End]) page and `enableLoop` is `false`.
 */
enum class VerticalPageSwitcherBoundary { Start, End }

/**
 * Alternative implementation of [VerticalPageSwitcher] that uses Compose's
 * [AnimatedContent] instead of an explicit per-page [androidx.compose.animation.AnimatedVisibility] loop.
 *
 * Behavior and transitions are intentionally identical to [VerticalPageSwitcher] so the
 * two can be A/B compared by swapping the call-site.
 *
 * **Key differences vs. [VerticalPageSwitcher]:**
 * - Only the currently visible page is composed (incoming + outgoing during the transition).
 *   The original eagerly composes every page and toggles visibility per index.
 * - The exit animation runs to completion before the outgoing page leaves composition,
 *   driven by [AnimatedContent]'s own transition machinery.
 * - Direction is read from a closed-over `direction` state and the transition spec re-runs
 *   on every target change.
 * - Adds [onBoundaryReached] so consumers can react when the visible page is the first or
 *   last page (analytics, "you're at the last page" hints, surrounding UI toggles).
 *
 * @param pages Composable lambdas to render. Must contain at least one entry.
 * @param modifier Modifier applied to the outer container.
 * @param spec Animation + behavior configuration.
 * @param onPageViewed Invoked once after the visible page index changes (including the initial value).
 * @param onBoundaryReached Invoked whenever the visible page is the first
 *   ([VerticalPageSwitcherBoundary.Start]) or last ([VerticalPageSwitcherBoundary.End]) page,
 *   provided `spec.enableLoop` is `false` (no boundary signal makes sense when looping).
 *   Fires on initial composition if `startIndex` lands on a boundary, and again every time
 *   navigation lands on one. Default is a no-op.
 * @param navigation Composable slot that renders navigation controls; receives click handlers
 *   and enable flags. Defaults to [VerticalPageSwitcherDefaults.NavButtons].
 */
@Composable
fun VerticalPageSwitcher_AnimatedContent(
    pages: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    spec: VerticalPageSwitcherSpec = VerticalPageSwitcherDefaults.Default,
    onPageViewed: (Int) -> Unit = {},
    onBoundaryReached: (VerticalPageSwitcherBoundary) -> Unit = {},
    navigation: VerticalPageSwitcherNavigationSlot = { onPrev, onNext, prevEnabled, nextEnabled ->
        VerticalPageSwitcherDefaults.NavButtons(
            onPrev = onPrev,
            onNext = onNext,
            prevEnabled = prevEnabled,
            nextEnabled = nextEnabled,
        )
    },
) {
    require(pages.isNotEmpty()) { "VerticalPageSwitcher1 requires at least one page" }

    val safeStartIndex = spec.startIndex.coerceIn(0, pages.lastIndex)
    var currentIndex by remember { mutableIntStateOf(safeStartIndex) }
    var direction by remember { mutableIntStateOf(1) }

    val onNext = {
        direction = 1
        when {
            currentIndex < pages.lastIndex -> currentIndex += 1
            spec.enableLoop -> currentIndex = 0
            // At the last page with looping disabled — no-op.
            // Boundary signaling is handled by the LaunchedEffect below so it fires
            // for every navigation slot (including the default disabled-button case).
            else -> Unit
        }
    }
    val onPrev = {
        direction = -1
        when {
            currentIndex > 0 -> currentIndex -= 1
            spec.enableLoop -> currentIndex = pages.lastIndex
            else -> Unit
        }
    }

    LaunchedEffect(currentIndex) { onPageViewed(currentIndex) }

    // Fires whenever the visible page IS a boundary (not "user tried to cross it").
    // Skipped entirely when looping, where the concept of a boundary doesn't apply.
    LaunchedEffect(currentIndex, spec.enableLoop, pages.lastIndex) {
        if (spec.enableLoop) return@LaunchedEffect
        when (currentIndex) {
            0 -> onBoundaryReached(VerticalPageSwitcherBoundary.Start)
            pages.lastIndex -> onBoundaryReached(VerticalPageSwitcherBoundary.End)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = { verticalPageTransition(spec.animationDurationMillis, direction) },
            label = "VerticalPageSwitcher1",
        ) { index ->
            pages[index]()
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

/**
 * Builds the direction-aware enter+exit transition used by [VerticalPageSwitcher_AnimatedContent].
 *
 * Mirrors the original animation:
 * - Enter: fade in + slide in vertically (down when going forward, up when going back).
 * - Exit:  fade out + slide out vertically (up when going forward, down when going back).
 */
private fun verticalPageTransition(
    durationMillis: Int,
    direction: Int,
): ContentTransform {
    val enter = fadeIn(animationSpec = tween(durationMillis)) +
        slideInVertically(
            animationSpec = tween(durationMillis),
            initialOffsetY = { if (direction > 0) it else -it },
        )
    val exit = fadeOut(animationSpec = tween(durationMillis)) +
        slideOutVertically(
            animationSpec = tween(durationMillis),
            targetOffsetY = { if (direction > 0) -it else it },
        )
    return enter togetherWith exit
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun VerticalPageSwitcher1DefaultNavPreview() {
    VerticalPageSwitcher1PreviewContent()
}

@Preview(showBackground = true)
@Composable
private fun VerticalPageSwitcher1CustomNavPreview() {
    VerticalPageSwitcher1PreviewContent(
        navigation = { onPrev, onNext, prevEnabled, nextEnabled ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onPrev, enabled = prevEnabled) { Text("Prev") }
                Button(onClick = onNext, enabled = nextEnabled) { Text("Next") }
            }
        },
    )
}

@Composable
private fun VerticalPageSwitcher1PreviewContent(
    navigation: VerticalPageSwitcherNavigationSlot? = null,
) {
    val pages = listOf<@Composable () -> Unit>(
        { CenteredText1("Page 1") },
        { CenteredText1("Page 2") },
        { CenteredText1("Page 3") },
    )
    val spec = VerticalPageSwitcherSpec(
        animationDurationMillis = 600,
        enableLoop = false,
        startIndex = 0,
    )
    val onBoundary: (VerticalPageSwitcherBoundary) -> Unit = { edge ->
        android.util.Log.d("VerticalPageSwitcher1", "Boundary reached: $edge")
    }
    if (navigation == null) {
        VerticalPageSwitcher_AnimatedContent(
            pages = pages,
            spec = spec,
            onBoundaryReached = onBoundary,
        )
    } else {
        VerticalPageSwitcher_AnimatedContent(
            pages = pages,
            spec = spec,
            navigation = navigation,
            onBoundaryReached = onBoundary,
        )
    }
}

@Composable
private fun CenteredText1(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text)
    }
}

