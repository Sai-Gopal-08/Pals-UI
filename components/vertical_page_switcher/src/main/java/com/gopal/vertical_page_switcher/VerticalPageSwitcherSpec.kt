package com.gopal.vertical_page_switcher

import androidx.compose.runtime.Immutable

/**
 * Immutable configuration for [VerticalPageSwitcher].
 *
 * Configuration only — callbacks (`onPageViewed`) and content slots (`navigation`)
 * are hoisted to the composable's parameter list.
 *
 * Use [VerticalPageSwitcherDefaults.Default] for the pre-built default.
 *
 * @property animationDurationMillis Duration of the vertical slide + fade transition.
 * @property enableLoop When true, advancing past the last page wraps to the first
 *   (and vice versa).
 * @property startIndex Initial page index. Read only on first composition; later
 *   changes are ignored — re-mount the composable to apply a new start index.
 */
@Immutable
data class VerticalPageSwitcherSpec(
    val animationDurationMillis: Int,
    val enableLoop: Boolean,
    val startIndex: Int,
)

