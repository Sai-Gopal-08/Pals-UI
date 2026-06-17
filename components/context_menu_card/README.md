# ContextMenuCard

A Jetpack Compose component that wraps a Material3 `Card` with a contextual `DropdownMenu` that appears precisely at the long-press location on the card. Menu visibility is managed internally — no external state management required.

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `ContextMenuCard` | `@Composable` | — | The component |
| `ContextMenuCardSpec` | `data class` | `@Immutable` | Appearance and behavior configuration |
| `ContextMenuCardState` | `data class` | `@Stable` | Runtime interaction state |
| `ContextMenuCardDefaults` | `object` | — | Pre-built default values |

---

## Basic Usage

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
    }
)
```

---

## With Callbacks

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(uniqueKeySpecificToCard = "my_card"),
    onCardClick = { cardId -> /* navigate */ },
    onLongPress = { cardId -> /* haptic feedback / analytics */ },
    onMenuDismissed = { /* cleanup */ },
    cardContent = { /* ... */ },
    dropDownColumnContent = { /* ... */ }
)
```

---

## Disable Interactions

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(uniqueKeySpecificToCard = "my_card"),
    state = ContextMenuCardState(areClicksEnabled = false),
    cardContent = { /* ... */ },
    dropDownColumnContent = { /* ... */ }
)
```

---

## Custom Appearance

```kotlin
ContextMenuCard(
    spec = ContextMenuCardSpec(
        uniqueKeySpecificToCard = "my_card",
        cardShape = RoundedCornerShape(8.dp),
        cardBorderStroke = BorderStroke(1.dp, Color.Gray),
        cardHoverAnimationSpec = tween(durationMillis = 150)
    ),
    cardContent = { /* ... */ },
    dropDownColumnContent = { /* ... */ }
)
```

---

## ContextMenuCardSpec Parameters

| Parameter | Type | Description | Default |
|---|---|---|---|
| `uniqueKeySpecificToCard` | `String` | Non-blank unique identifier for gesture scoping | required |
| `cardShape` | `Shape?` | Card corner shape | `RoundedCornerShape(16.dp)` |
| `cardColors` | `CardColors?` | Card container and content colors | Material3 defaults |
| `cardHoverAnimationSpec` | `AnimationSpec<Float>?` | Scale animation when menu opens | `tween(250ms)` |
| `hoveredCardElevation` | `CardElevation?` | Elevation when menu is visible | `8.dp` |
| `defaultCardElevation` | `CardElevation?` | Elevation when menu is hidden | `2.dp` |
| `cardBorderStroke` | `BorderStroke?` | Optional card border | none |

---

## Module

```
:components:context_menu_card
```

Namespace: `com.gopal.context_menu_card`

