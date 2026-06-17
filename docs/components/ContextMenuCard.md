# ContextMenuCard

## Overview

`ContextMenuCard` is a Jetpack Compose component that combines a Material3 `Card` with a `DropdownMenu` that appears precisely at the long-press location on the card. Menu visibility is managed **automatically and internally** — no external state management is required for showing or hiding the menu.

---

## Module

```
:components:context_menu_card
```

| Property | Value |
|---|---|
| Namespace | `com.gopal.context_menu_card` |
| Min SDK | 24 |
| Type | Android Library |

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `ContextMenuCard` | `@Composable` | — | Entry point composable |
| `ContextMenuCardSpec` | `data class` | `@Immutable` | Appearance and behavior configuration |
| `ContextMenuCardState` | `data class` | `@Stable` | Runtime interaction state |
| `ContextMenuCardDefaults` | `object` | — | Pre-built default values |

### ContextMenuCard

```kotlin
@Composable
fun ContextMenuCard(
    spec: ContextMenuCardSpec,
    modifier: Modifier = Modifier,
    state: ContextMenuCardState = ContextMenuCardState(),
    onCardClick: ((String) -> Unit)? = null,
    onLongPress: ((String) -> Unit)? = null,
    onMenuDismissed: (() -> Unit)? = null,
    cardContent: @Composable BoxScope.() -> Unit,
    dropDownColumnContent: @Composable ColumnScope.() -> Unit
)
```

### ContextMenuCardSpec

```kotlin
@Immutable
data class ContextMenuCardSpec(
    val uniqueKeySpecificToCard: String,          // required, must be non-blank
    val cardShape: Shape? = null,
    val cardColors: CardColors? = null,
    val cardHoverAnimationSpec: AnimationSpec<Float>? = null,
    val hoveredCardElevation: CardElevation? = null,
    val defaultCardElevation: CardElevation? = null,
    val cardBorderStroke: BorderStroke? = null
)
```

### ContextMenuCardState

```kotlin
@Stable
data class ContextMenuCardState(
    val areClicksEnabled: Boolean = true
)
```

---

## Default Values

Provided by `ContextMenuCardDefaults`:

| Property | Default |
|---|---|
| `cardShape` | `RoundedCornerShape(16.dp)` |
| `cardColors` | `CardDefaults.cardColors()` (Material3 theme) |
| `cardHoverAnimationSpec` | `tween(durationMillis = 250)` |
| `hoveredCardElevation` | `CardDefaults.elevatedCardElevation(8.dp)` |
| `defaultCardElevation` | `CardDefaults.cardElevation(2.dp)` |

> `cardColors`, `hoveredCardElevation`, and `defaultCardElevation` are `@Composable` getters because they depend on the Material3 theme. This is intentional and unavoidable.

---

## Usage Examples

### Basic

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(uniqueKeySpecificToCard = "my_card"),
    cardContent = {
        Text(
            text = "Long press to open menu",
            modifier = Modifier.padding(16.dp)
        )
    },
    dropDownColumnContent = {
        DropdownMenuItem(text = { Text("Edit") }, onClick = { })
        DropdownMenuItem(text = { Text("Delete") }, onClick = { })
        DropdownMenuItem(text = { Text("Share") }, onClick = { })
    }
)
```

### With all callbacks

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(uniqueKeySpecificToCard = "my_card"),
    onCardClick = { cardId -> /* navigate to detail */ },
    onLongPress = { cardId -> /* haptic feedback, analytics */ },
    onMenuDismissed = { /* cleanup */ },
    cardContent = { /* ... */ },
    dropDownColumnContent = { /* ... */ }
)
```

### Disable interactions (e.g. during loading)

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(uniqueKeySpecificToCard = "my_card"),
    state = ContextMenuCardState(areClicksEnabled = false),
    cardContent = { /* ... */ },
    dropDownColumnContent = { /* ... */ }
)
```

### Custom appearance

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(
        uniqueKeySpecificToCard = "my_card",
        cardShape = RoundedCornerShape(8.dp),
        cardBorderStroke = BorderStroke(1.dp, Color.LightGray),
        cardHoverAnimationSpec = tween(durationMillis = 150)
    ),
    cardContent = { /* ... */ },
    dropDownColumnContent = { /* ... */ }
)
```

---

## Architecture Decisions

### Why is menu visibility managed internally?

Menu visibility is pure UI state — it is always shown on long press and hidden on dismiss with no business logic involved. Exposing it externally would add unnecessary boilerplate for every consumer. Callbacks (`onLongPress`, `onMenuDismissed`) allow side effects without leaking internal state.

### Why `@Immutable` on `ContextMenuCardSpec`?

The spec is pure configuration — no mutable state, no business logic, no callbacks. `@Immutable` lets the Compose compiler skip recompositions when the spec hasn't changed.

### Why `@Stable` on `ContextMenuCardState`?

The ADR requires `@Stable` on all State classes. Although Compose can infer stability for a `data class` with only stable properties, the explicit annotation makes the contract clear and guarantees the compiler treats it as stable without relying on inference.

### Why direct lambdas instead of a Callbacks class?

The component has 3 optional callbacks — below the ADR threshold of 4+. Direct lambdas keep the API minimal and readable.

### Why `uniqueKeySpecificToCard` is required and validated?

It is used as the key for the `pointerInput` modifier. If it were blank or shared between cards, gesture handlers would be incorrectly scoped — causing wrong cards to react to gestures in a list.

---

## File Structure

```
components/context_menu_card/
├── src/main/java/com/gopal/context_menu_card/
│   ├── ContextMenuCard.kt          # Public composable + internal logic
│   ├── ContextMenuCardSpec.kt      # @Immutable configuration data class
│   ├── ContextMenuCardState.kt     # @Stable runtime interaction state
│   └── ContextMenuCardDefaults.kt  # Pre-built default values
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
- [x] Naming follows library conventions (`ContextMenuCard` prefix on all public types)
- [x] Spec exists and is `@Immutable`
- [x] Spec contains configuration only
- [x] State exists and is `@Stable`
- [x] State is properly hoisted (passed as parameter with a default)
- [x] Small callback count uses direct lambdas (3 callbacks, threshold is 4+)
- [x] Flat structure used
- [x] No generic model/common/data folders
- [x] `kotlin.compose` plugin applied
- [x] All composable parameters are stable types — UI recomposes only when inputs genuinely change
- [ ] Public API reviewed for Maven publication
- [ ] Component ready for Maven publication

