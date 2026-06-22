# PillTabRow

A pill-shaped tab row with an animated sliding indicator for Jetpack Compose.

Tabs divide the pill width equally. The indicator animates smoothly between tabs using a configurable `AnimationSpec`. Tab shapes (rounded ends for first/last, square for middle) are computed automatically.

---

## Module

```
:components:pill_tab_row
```

**Namespace:** `com.gopal.pill_tab_row`  
**Min SDK:** 24

---

## Basic Usage

```kotlin
var selectedTab by remember { mutableIntStateOf(0) }

PillTabRow(
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
    spec = PillTabRowSpec(
        titles = listOf("Day", "Week", "Month"),
        pillHeight = 48.dp,
        pillCornerRadius = 24.dp,
        pillTitleFontSize = 16.sp,
        colors = PillTabRowColors(
            backgroundColor = Color.LightGray,
            indicatorColor = Color.Blue,
            textColor = Color.Black,
            selectedTextColor = Color.White,
        ),
        borderSpec = PillBorderSpec(borderColor = Color.Black, borderWidth = 2.dp),
        indicatorAnimationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
    )
)
```

---

## Using Defaults

```kotlin
PillTabRow(
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
    spec = PillTabRowDefaults.defaultPillTabRowSpec,
)
```

---

## Custom Colors

```kotlin
PillTabRow(
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
    spec = PillTabRowDefaults.defaultPillTabRowSpec.copy(
        colors = PillTabRowColors(
            backgroundColor = Color(0xFFEEEEEE),
            indicatorColor = Color(0xFF6200EE),
            textColor = Color.DarkGray,
            selectedTextColor = Color.White,
        )
    )
)
```

---

## Public API

| Type | Role |
|---|---|
| `PillTabRow` | Main composable |
| `PillTabRowSpec` | Full configuration (`@Immutable`) |
| `PillTabRowColors` | Background, indicator, text colors (`@Immutable`) |
| `PillBorderSpec` | Tab segment border (`@Immutable`) |
| `PillTabRowDefaults` | Pre-built defaults |

### `PillTabRowDefaults`

| Member | Type | Detail |
|---|---|---|
| `defaultPillTabRowSpec` | `PillTabRowSpec` | Full default spec (3 tabs, blue indicator) |
| `firstPillShape` | `Shape` | Rounded-left shape for first tab |
| `lastPillShape` | `Shape` | Rounded-right shape for last tab |
| `middlePillShape` | `Shape` | Square shape for middle tabs |
| `fallBackDpValue` | `Dp` | `0.dp` — used before layout is measured |
| `minimumSpacingAroundPillTitle` | `Dp` | `4.dp` horizontal padding on tab labels |

