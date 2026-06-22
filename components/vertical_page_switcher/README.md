# VerticalPageSwitcher

A Jetpack Compose component that displays one page at a time and animates
vertically (slide + cross-fade) between pages. Navigation UI is fully
delegated to a composable slot.

Part of the **PalsUI** component library.

## Why

`HorizontalPager` exists, but a guided, single-page-at-a-time vertical flow
(onboarding, slideshows, step-through tutorials) is awkward to build on top
of it. This component handles the index state, the direction-aware transition,
and the navigation slot in one place.

## Usage

### Defaults (Previous / Next buttons)

```kotlin
VerticalPageSwitcher(
    pages = listOf(
        { Page1() },
        { Page2() },
        { Page3() },
    ),
)
```

### Custom navigation slot

```kotlin
VerticalPageSwitcher(
    pages = pages,
    spec = VerticalPageSwitcherSpec(
        animationDurationMillis = 400,
        enableLoop = true,
        startIndex = 0,
    ),
    onPageViewed = { index -> analytics.trackPage(index) },
    navigation = { onPrev, onNext, prevEnabled, nextEnabled ->
        MyNavRow(
            onPrev = onPrev,
            onNext = onNext,
            prevEnabled = prevEnabled,
            nextEnabled = nextEnabled,
        )
    },
)
```

### Reusing the default `NavButtons` with custom labels

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
            nextLabel = "Continue",
        )
    },
)
```

## API

### `VerticalPageSwitcher`

```kotlin
@Composable
fun VerticalPageSwitcher(
    pages: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    spec: VerticalPageSwitcherSpec = VerticalPageSwitcherDefaults.Default,
    onPageViewed: (Int) -> Unit = {},
    navigation: VerticalPageSwitcherNavigationSlot = { … default NavButtons … },
)
```

### `VerticalPageSwitcherSpec`

| Property                  | Type    | Default | Description                                                 |
|---------------------------|---------|---------|-------------------------------------------------------------|
| `animationDurationMillis` | `Int`   | `800`   | Duration of the slide + fade transition.                    |
| `enableLoop`              | `Boolean` | `false` | When `true`, last → first (and vice versa) wraps around.  |
| `startIndex`              | `Int`   | `0`     | Initial page index (read once on first composition).        |

### `VerticalPageSwitcherDefaults`

| Member                              | Kind        | Purpose                                                          |
|-------------------------------------|-------------|------------------------------------------------------------------|
| `Default`                           | `val`       | Pre-built spec; allocated once, zero per-recomposition cost.     |
| `DefaultAnimationDurationMillis`    | `const Int` | The 800-ms default exposed for callers building a custom spec.   |
| `NavButtons(onPrev, onNext, ...)`   | `@Composable` | Ready-made "Previous"/"Next" Material button row for the slot. |

### `VerticalPageSwitcherNavigationSlot`

Type alias for the navigation slot signature:

```kotlin
typealias VerticalPageSwitcherNavigationSlot = @Composable (
    onPrev: () -> Unit,
    onNext: () -> Unit,
    prevEnabled: Boolean,
    nextEnabled: Boolean,
) -> Unit
```

The switcher computes `prevEnabled` / `nextEnabled` from the current index,
the last index, and `spec.enableLoop`, so the slot doesn't need to re-derive
boundary state.

## Notes

- `VerticalPageSwitcherSpec` is `@Immutable`; the composable is skippable.
- `VerticalPageSwitcherDefaults.Default` is a plain singleton `val` — no
  composable getter allocation on every recomposition.
- The internal `currentIndex` / `direction` state is owned by the composable;
  programmatic external control is not yet supported (no hoisted `State`).

