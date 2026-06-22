# VerticalPageSwitcher

## Overview

`VerticalPageSwitcher` is a Jetpack Compose component that displays one page at a time from a list of composable pages, animating between them with a directional vertical slide and cross-fade. Navigation UI is delegated to a composable slot so the caller can render anything — the default slot supplies a "Previous"/"Next" Material button row.

---

## Module

```
:components:vertical_page_switcher
```

| Property | Value |
|---|---|
| Namespace | `com.gopal.vertical_page_switcher` |
| Min SDK | 24 |
| Type | Android Library |

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `VerticalPageSwitcher` | `@Composable` | — | Entry point composable |
| `VerticalPageSwitcherSpec` | `data class` | `@Immutable` | Behaviour configuration (duration, loop, start index) |
| `VerticalPageSwitcherDefaults` | `object` | — | Singleton default spec + a ready-made `NavButtons` composable slot |
| `VerticalPageSwitcherNavigationSlot` | `typealias` | — | Signature of the `navigation` content slot |

### VerticalPageSwitcher

```kotlin
@Composable
fun VerticalPageSwitcher(
    pages: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    spec: VerticalPageSwitcherSpec = VerticalPageSwitcherDefaults.Default,
    onPageViewed: (Int) -> Unit = {},
    navigation: VerticalPageSwitcherNavigationSlot = { … default NavButtons … }
)
```

### VerticalPageSwitcherSpec

```kotlin
@Immutable
data class VerticalPageSwitcherSpec(
    val animationDurationMillis: Int,
    val enableLoop: Boolean,
    val startIndex: Int
)
```

### VerticalPageSwitcherNavigationSlot

```kotlin
typealias VerticalPageSwitcherNavigationSlot = @Composable (
    onPrev: () -> Unit,
    onNext: () -> Unit,
    prevEnabled: Boolean,
    nextEnabled: Boolean
) -> Unit
```

---

## Default Values

`VerticalPageSwitcherDefaults.Default` is a plain singleton `val` — allocated once at class load, zero per-recomposition cost.

| Property | Default |
|---|---|
| `animationDurationMillis` | `800` ms (exposed as `DefaultAnimationDurationMillis`) |
| `enableLoop` | `false` |
| `startIndex` | `0` |

---

## Usage Examples

### Basic — use defaults

```kotlin
VerticalPageSwitcher(
    pages = listOf(
        { Page1() },
        { Page2() },
        { Page3() }
    )
)
```

### Custom spec + viewed callback

```kotlin
VerticalPageSwitcher(
    pages = pages,
    spec = VerticalPageSwitcherSpec(
        animationDurationMillis = 400,
        enableLoop = true,
        startIndex = 0
    ),
    onPageViewed = { index -> analytics.trackPage(index) }
)
```

### Custom navigation slot

```kotlin
VerticalPageSwitcher(
    pages = pages,
    navigation = { onPrev, onNext, prevEnabled, nextEnabled ->
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = onPrev, enabled = prevEnabled) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            IconButton(onClick = onNext, enabled = nextEnabled) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }
    }
)
```

### Reuse default `NavButtons` with custom labels

```kotlin
VerticalPageSwitcher(
    pages = pages,
    navigation = { onPrev, onNext, prevEnabled, nextEnabled ->
        VerticalPageSwitcherDefaults.NavButtons(
            onPrev = onPrev,
            onNext = onNext,
            prevEnabled = prevEnabled,
            nextEnabled = nextEnabled,
            prevLabel = "Back",
            nextLabel = "Continue"
        )
    }
)
```

---

## Architecture Decisions

### Why a slot for navigation instead of a sealed `NavigationSpec`?

The original API used a `sealed class NavigationSpec { NavigationButtons; CustomContent }` stored on the spec, which mixed two distinct concerns — configuration (durations, loop, indices) and UI content (composable lambdas). Per the ADR Spec Guidelines, specs hold configuration only — no composables, no callbacks. Lifting navigation to a top-level slot keeps the spec purely declarative and gives callers full content power without the indirection of two sealed subtypes.

### Why a typealias for the slot signature?

`VerticalPageSwitcherNavigationSlot` documents the four parameters the switcher passes (handlers + enable flags) once, in one place, instead of repeating the function type at every call-site. The switcher computes `prevEnabled` / `nextEnabled` from `currentIndex`, `lastIndex`, and `spec.enableLoop`, so slot authors don't re-derive boundary logic.

### Why is `Default` a plain `val` and not a `@Composable` getter?

`VerticalPageSwitcherSpec` no longer contains any composable lambdas or theme reads, so there is nothing about it that requires a composition scope. A plain `val` is allocated once and reused freely; a `@Composable` getter would re-allocate the spec (and any nested objects) on every recomposition.

### Why `@Immutable` on the spec?

`VerticalPageSwitcherSpec` is a `data class` of primitives — every field is structurally comparable. `@Immutable` tells the Compose compiler the type is fully stable, so the switcher composable becomes skippable when the same spec is passed across recompositions.

### Why is `onPageViewed` not on the spec?

Per the ADR Callback Guidelines, a single callback should be a direct top-level parameter, not a field on a config data class. Putting callbacks on the spec also forces consumers to copy the spec just to change the listener.

### Why `LaunchedEffect(currentIndex) { onPageViewed(currentIndex) }`?

`onPageViewed` is fired exactly once per index change, including the initial page, with side-effect semantics decoupled from rendering. Placing it inside `LaunchedEffect` guarantees it runs in a composition-aware coroutine scope and is cancelled cleanly if the composable leaves the tree mid-animation.

### What's intentionally not here (yet)

- **Hoisted state.** `currentIndex` / `direction` are private `remember`-state. External programmatic control (e.g., from a sibling tab row, or for restoration) would require a `@Stable class VerticalPageSwitcherState` and `rememberVerticalPageSwitcherState()`. Not in this revision.
- **Lazy page content.** `pages: List<@Composable () -> Unit>` materialises every page in the composition tree. For heavy pages or unbounded lists, a `pageCount: Int, pageContent: @Composable (Int) -> Unit` slot would be required.

---

## File Structure

```
components/vertical_page_switcher/
├── src/main/java/com/gopal/vertical_page_switcher/
│   ├── VerticalPageSwitcher.kt           # Public composable + previews
│   ├── VerticalPageSwitcherSpec.kt       # @Immutable configuration data class
│   └── VerticalPageSwitcherDefaults.kt   # Default spec val + NavButtons composable slot
├── build.gradle.kts
└── README.md
```

---

## Validation Checklist

- [x] Component has its own module
- [x] README exists
- [x] Component documentation added to `docs/components/`
- [ ] Sample/demo exists in showcase app
- [x] Public API is minimal
- [x] Implementation details are hidden
- [x] Naming follows library conventions (`VerticalPageSwitcher` prefix on all public types)
- [x] Spec exists and is `@Immutable`
- [x] Spec contains configuration only (callbacks + slots are top-level parameters)
- [ ] State is properly hoisted (currently private — flagged for a future revision)
- [x] Small callback count uses direct lambdas
- [x] All composable parameters are stable types
- [ ] No unnecessary recompositions — `onPrev`/`onNext` lambdas + per-recomposition `tween(...)` allocations remain (flagged for follow-up)
- [x] `@Stable` / `@Immutable` annotations reviewed
- [ ] No avoidable allocations during recomposition (see above)
- [x] Flat structure used
- [x] No generic model/common/data folders
- [ ] Public API reviewed for Maven publication
- [ ] Component ready for Maven publication

