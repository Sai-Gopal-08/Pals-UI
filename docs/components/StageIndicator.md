# StageIndicator

## Overview

A horizontal row of indicator dots representing pages or stages in a flow.

Dots at or before `currentIndex` are rendered in the filled color; dots beyond it use the unfilled
color. Each dot's width is dynamically computed via `onGloballyPositioned` to fill the available
row width evenly — no fixed sizes required from the caller.

**Module:** `:components:stage_indicator`  
**Package:** `com.gopal.stage_indicator`  
**Min SDK:** 24

---

## Public API

### `StageIndicator`

```kotlin
@Composable
fun StageIndicator(
    currentIndex: Int,
    modifier: Modifier = Modifier,
    spec: StageIndicatorSpec = StageIndicatorDefaults.spec(),
)
```

| Parameter | Type | Detail |
|---|---|---|
| `currentIndex` | `Int` | Zero-based index of the active page — dots ≤ this index are filled |
| `modifier` | `Modifier` | Applied to the indicator row |
| `spec` | `StageIndicatorSpec` | Full appearance configuration |

---

### `StageIndicatorSpec` (`@Immutable`)

```kotlin
@Immutable
data class StageIndicatorSpec(
    val totalPages: Int,
    val indicatorSpacing: Dp,
    val indicatorHeight: Dp,
    val stageIndicatorColors: StageIndicatorColors,
    val indicatorBorderSpec: StageIndicatorBorderSpec,
)
```

---

### `StageIndicatorColors` (`@Immutable`)

```kotlin
@Immutable
data class StageIndicatorColors(
    val filledIndicatorColor: Color,   // dots at or before currentIndex
    val unfilledIndicatorColor: Color, // dots after currentIndex
)
```

---

### `StageIndicatorBorderSpec` (`@Immutable`)

```kotlin
@Immutable
data class StageIndicatorBorderSpec(
    val borderWidth: Dp,
    val borderColor: Color,
)
```

---

### `StageIndicatorDefaults`

| Member | Returns | Detail |
|---|---|---|
| `indicatorSpacing` | `Dp` | `16.dp` — static singleton |
| `indicatorHeight` | `Dp` | `8.dp` — static singleton |
| `borderWidth` | `Dp` | `1.dp` — static singleton |
| `spec()` | `@Composable StageIndicatorSpec` | Full spec from `MaterialTheme` |
| `colors(...)` | `StageIndicatorColors` | Factory for custom color pairs |

---

## Usage Examples

### Basic (3 pages, index 1 filled)

```kotlin
StageIndicator(currentIndex = 1)
```

### Custom page count

```kotlin
StageIndicator(
    currentIndex = 3,
    spec = StageIndicatorDefaults.spec().copy(totalPages = 5)
)
```

### Custom colors

```kotlin
StageIndicator(
    currentIndex = 0,
    spec = StageIndicatorDefaults.spec().copy(
        stageIndicatorColors = StageIndicatorColors(
            filledIndicatorColor = Color.Blue,
            unfilledIndicatorColor = Color.LightGray,
        )
    )
)
```

### Custom border

```kotlin
StageIndicator(
    currentIndex = 1,
    spec = StageIndicatorDefaults.spec().copy(
        indicatorBorderSpec = StageIndicatorBorderSpec(
            borderWidth = 2.dp,
            borderColor = Color.DarkGray,
        )
    )
)
```

### Fill the available width in a full-width container

```kotlin
StageIndicator(
    currentIndex = 1,
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
)
```

---

## Architecture Decisions

### Why `@Immutable` on all spec data classes?
`StageIndicatorSpec` contains nested `StageIndicatorColors` and `StageIndicatorBorderSpec`.
Without `@Immutable`, the Compose compiler cannot guarantee stability, and `StageIndicator`
would recompose on every parent recomposition even when nothing visually changed.

### Why `@Composable fun spec()` instead of a getter?
Follows the established PalsUI convention. A `@Composable` function is explicit about requiring
a composition scope and mirrors Material3's own defaults pattern. The `@Composable` getter
is an unusual Kotlin construct that surprises contributors.

### Why internal `indicatorWidth` state via `onGloballyPositioned`?
Each dot's width must fill the available row width evenly. Since the row's pixel width is only
known after layout, `onGloballyPositioned` is used to capture the measured width and divide it
by `totalPages`. The state is internal and correctly not hoisted — callers only need to provide
`currentIndex`, not manage layout details.

### Why convert spacing to px inside `onGloballyPositioned`?
`onGloballyPositioned` operates in **pixel** space. `indicatorSpacing` is in `Dp`. Using
`spacingDp.value.toInt()` would read the raw float as pixels (incorrect on non-1x density screens).
The correct conversion is `with(density) { totalSpacing.toPx() }.toInt()`.

---

## File Structure

```
stage_indicator/
├── StageIndicator.kt          # Public composable
├── StageIndicatorSpec.kt      # @Immutable spec types
├── StageIndicatorDefaults.kt  # Default values
└── README.md
```

---

## Validation Checklist

### Module
- [x] Component has its own module
- [x] README exists
- [x] Component documentation added to `docs/components/`
- [ ] Sample/demo exists in showcase app

### API Design
- [x] Public API is minimal — only `StageIndicator()` is public
- [x] Implementation details hidden — preview is `private`
- [x] Naming follows library conventions — `StageIndicator*` matches module `stage_indicator`

### Spec
- [x] Spec exists — `StageIndicatorSpec`
- [x] Spec is immutable — `@Immutable` on all 3 spec types
- [x] Spec contains configuration only

### State
- [x] State exists only if needed — internal `indicatorWidth` justified by dynamic layout
- [x] State is properly hoisted — `currentIndex` owned by caller

### Callbacks
- [x] No callbacks needed

### Compose Performance
- [x] All composable parameters are stable — `@Immutable` on all spec types
- [x] No unnecessary recompositions
- [x] Stable/Immutable annotations reviewed
- [x] No avoidable allocations

### Structure
- [x] Flat structure — 3 files, no premature folders
- [x] No generic folders

### Publishing
- [ ] Public API reviewed
- [ ] Documentation completed
- [ ] Component ready for Maven publication

