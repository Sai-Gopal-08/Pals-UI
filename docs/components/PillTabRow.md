# PillTabRow

## Overview

A pill-shaped tab row with an animated sliding indicator.

Tabs share the available width equally. The sliding indicator animates between positions using
a configurable `AnimationSpec<Dp>`. Tab and indicator shapes are computed internally — first/last
tabs get rounded ends, middle tabs are square. Tab positions are measured via `onGloballyPositioned`
and cached in a `SnapshotStateList`; the indicator only renders after all tabs are measured.

**Module:** `:components:pill_tab_row`  
**Package:** `com.gopal.pill_tab_row`  
**Min SDK:** 24

---

## Public API

### `PillTabRow`

```kotlin
@Composable
fun PillTabRow(
    onTabSelected: (Int) -> Unit,
    selectedIndex: Int,
    spec: PillTabRowSpec,
    modifier: Modifier = Modifier,
)
```

| Parameter | Type | Detail |
|---|---|---|
| `onTabSelected` | `(Int) -> Unit` | Called with the index of the tapped tab |
| `selectedIndex` | `Int` | Zero-based index of the active tab |
| `spec` | `PillTabRowSpec` | Full appearance and animation configuration |
| `modifier` | `Modifier` | Applied to the outer pill container |

**Requires:** `spec.titles.size >= 2`

---

### `PillTabRowSpec` (`@Immutable`)

```kotlin
@Immutable
data class PillTabRowSpec(
    val pillHeight: Dp,
    val pillCornerRadius: Dp,
    val pillTitleFontSize: TextUnit,
    val colors: PillTabRowColors,
    val borderSpec: PillBorderSpec,
    val indicatorAnimationSpec: AnimationSpec<Dp>,
    val titles: List<String>,
)
```

---

### `PillTabRowColors` (`@Immutable`)

```kotlin
@Immutable
data class PillTabRowColors(
    val backgroundColor: Color,
    val indicatorColor: Color,
    val textColor: Color,
    val selectedTextColor: Color,
)
```

---

### `PillBorderSpec` (`@Immutable`)

```kotlin
@Immutable
data class PillBorderSpec(
    val borderColor: Color,
    val borderWidth: Dp,
)
```

---

### `PillTabRowDefaults`

| Member | Type | Detail |
|---|---|---|
| `defaultPillTabRowSpec` | `PillTabRowSpec` | Full default spec — `48.dp` height, `24.dp` radius, 3 tabs |
| `firstPillShape` | `Shape` | `RoundedCornerShape` — rounded left end |
| `lastPillShape` | `Shape` | `RoundedCornerShape` — rounded right end |
| `middlePillShape` | `Shape` | `RoundedCornerShape(0.dp)` — square |
| `fallBackDpValue` | `Dp` | `0.dp` — indicator target before first layout |
| `minimumSpacingAroundPillTitle` | `Dp` | `4.dp` — horizontal padding on tab labels |

---

## Usage Examples

### Basic with defaults

```kotlin
var selected by remember { mutableIntStateOf(0) }

PillTabRow(
    selectedIndex = selected,
    onTabSelected = { selected = it },
    spec = PillTabRowDefaults.defaultPillTabRowSpec,
)
```

### Custom titles and colors

```kotlin
PillTabRow(
    selectedIndex = selected,
    onTabSelected = { selected = it },
    spec = PillTabRowDefaults.defaultPillTabRowSpec.copy(
        titles = listOf("Day", "Week", "Month"),
        colors = PillTabRowColors(
            backgroundColor = Color(0xFFEEEEEE),
            indicatorColor = Color(0xFF6200EE),
            textColor = Color.DarkGray,
            selectedTextColor = Color.White,
        )
    )
)
```

### Slow animation

```kotlin
PillTabRow(
    selectedIndex = selected,
    onTabSelected = { selected = it },
    spec = PillTabRowDefaults.defaultPillTabRowSpec.copy(
        indicatorAnimationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
    )
)
```

---

## Architecture Decisions

### Why `@Immutable` on all spec data classes?
`PillTabRowSpec` contains `AnimationSpec<Dp>` (an interface) and `List<String>` (an interface).
The Compose compiler cannot infer stability for interface fields. Without `@Immutable`, the compiler
treats `PillTabRowSpec` as unstable, causing `PillTabRow` to recompose on every parent recomposition
even when the spec is unchanged. `@Immutable` instructs the compiler to trust structural equality.

### Why were `isTabLayoutStable` and `getShapeBasedOnIndex` removed from `PillTabRowSpec`?
Per the ADR, a Spec is "configuration only — no business logic". These were rendering helpers that
belonged to the composable's internal logic. Moving them to `private` functions in `PillTabRow.kt`
keeps the spec as a pure data container and hides implementation details from callers.

### Why is the indicator only rendered after all tabs are measured?
`tabPositions` is populated via `onGloballyPositioned`. Until all `sectionCount` positions are
known, the indicator's start/width targets are `0.dp`. Rendering before that would show a
momentary flash at position zero. The `tabPositions.size == sectionCount` guard prevents this.

### Why `derivedStateOf` for `currentTabIndex` and `indicatorShape`?
Both values are derived from animated `Dp` state (`indicatorStart`, `indicatorWidth`). Using
`derivedStateOf` means these only recompute when their inputs change, not on every frame of the
animation — preventing unnecessary sub-tree recompositions.

---

## File Structure

```
pill_tab_row/
├── PillTabRow.kt          # Public composable + private helpers
├── PillTabRowSpec.kt      # @Immutable spec types
├── PillTabRowDefaults.kt  # Default values
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
- [x] Public API is minimal — only `PillTabRow()` is public
- [x] Implementation details hidden — `isTabLayoutStable`, `getShapeBasedOnIndex`, `drawBordersAroundEachPill` are all `private`
- [x] Naming follows library conventions — `PillTabRow*` matches module `pill_tab_row`

### Spec
- [x] Spec exists — `PillTabRowSpec`
- [x] Spec is immutable — `@Immutable` on all 3 data classes
- [x] Spec contains configuration only — business logic methods removed

### State
- [x] State exists only if needed — `tabPositions` and animation state are necessary
- [x] State is properly hoisted — `selectedIndex` + `onTabSelected` owned by caller

### Callbacks
- [x] Small callback count — 1 callback `onTabSelected: (Int) -> Unit`, direct lambda

### Compose Performance
- [x] All composable parameters are stable — `@Immutable` on all spec types
- [x] No unnecessary recompositions — `derivedStateOf` used for all derived values
- [x] Stable/Immutable annotations reviewed
- [x] No avoidable allocations — `Pair` in `onGloballyPositioned` only on layout changes

### Structure
- [x] Flat structure — 3 files, no folders
- [x] No generic folders

### Publishing
- [ ] Public API reviewed
- [ ] Documentation completed
- [ ] Component ready for Maven publication

