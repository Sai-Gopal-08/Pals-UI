# StageIndicator

A horizontal row of indicator dots for Jetpack Compose, representing pages or stages in a flow.

Dots at or before the current index are rendered in the filled color; dots beyond it use the unfilled color. Each dot width is calculated dynamically to fill the available row width evenly.

---

## Module

```
:components:stage_indicator
```

**Namespace:** `com.gopal.stage_indicator`  
**Min SDK:** 24

---

## Basic Usage

```kotlin
StageIndicator(currentIndex = 1)
```

---

## Custom Page Count

```kotlin
StageIndicator(
    currentIndex = 2,
    spec = StageIndicatorDefaults.spec().copy(totalPages = 5)
)
```

---

## Custom Colors

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

---

## Custom Border

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

---

## Public API

| Type | Role |
|---|---|
| `StageIndicator` | Main composable |
| `StageIndicatorSpec` | Full configuration (`@Immutable`) |
| `StageIndicatorColors` | Filled / unfilled dot colors (`@Immutable`) |
| `StageIndicatorBorderSpec` | Dot border width + color (`@Immutable`) |
| `StageIndicatorDefaults` | Material3-themed defaults |

### `StageIndicatorDefaults`

| Member | Type | Detail |
|---|---|---|
| `indicatorSpacing` | `Dp` | `16.dp` — static singleton |
| `indicatorHeight` | `Dp` | `8.dp` — static singleton |
| `borderWidth` | `Dp` | `1.dp` — static singleton |
| `spec()` | `@Composable` fun | Full spec from `MaterialTheme` |
| `colors(...)` | fun | `StageIndicatorColors` factory |

