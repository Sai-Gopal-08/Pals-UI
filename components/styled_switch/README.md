# StyledSwitch

A fully customisable animated toggle switch for Jetpack Compose.

Supports custom colors, shapes, elevation, borders, and optional thumb icons from any source type
(vector, drawable resource, bitmap resource, or raw bitmap via `FlexibleIconSource`).

---

## Module

```
:components:styled_switch
```

**Namespace:** `com.gopal.styled_switch`  
**Min SDK:** 24

---

## Basic Usage

```kotlin
var checked by remember { mutableStateOf(false) }

StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it }
)
```

---

## Custom Colors

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        colors = StyledSwitchDefaults.colors().copy(
            checkedTrackColor = Color(0xFF4CAF50),
            uncheckedTrackColor = Color(0xFFBDBDBD)
        )
    )
)
```

---

## Custom Shape and Size

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        thumbSize = 40.dp,
        trackShape = RoundedCornerShape(8.dp)
    )
)
```

---

## With Border

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        borderSpec = StyledSwitchBorderSpec(
            borderColor = Color.Gray,
            borderWidth = 1.dp
        )
    )
)
```

---

## With Thumb Icons

Uses `FlexibleIconSource` — supports any icon type:

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        icons = StyledSwitchIcons(
            checkedIcon = FlexibleIconSource.VectorIcon(
                imageVector = Icons.Default.Check,
                contentDescriptionResource = R.string.checked
            ),
            uncheckedIcon = FlexibleIconSource.VectorIcon(
                imageVector = Icons.Default.Close,
                contentDescriptionResource = R.string.unchecked
            )
        )
    )
)
```

---

## Public API

| Type | Role |
|---|---|
| `StyledSwitch` | Main composable |
| `StyledSwitchSpec` | Full configuration (`@Immutable`) |
| `StyledSwitchBorderSpec` | Thumb border color + width (`@Immutable`) |
| `StyledSwitchIcons` | Checked/unchecked thumb icons via `FlexibleIconSource` (`@Immutable`) |
| `StyledSwitchColors` | Track, thumb, and icon tint colors (`@Immutable`) |
| `StyledSwitchDefaults` | Pre-built Material3-themed defaults |

### `StyledSwitchDefaults`

| Member | Type | Detail |
|---|---|---|
| `thumbSize` | `Dp` | `32.dp` — static singleton |
| `elevation` | `Dp` | `4.dp` — static singleton |
| `trackShape` | `Shape` | `CircleShape` — static singleton |
| `borderSpec` | `StyledSwitchBorderSpec` | Transparent, 0dp — static singleton |
| `icons` | `StyledSwitchIcons?` | `null` by default |
| `colors()` | `@Composable` fun | Material3 `colorScheme` colors |
| `spec()` | `@Composable` fun | Full spec combining all defaults |

---

## Dependencies

- `:components:flexible_icon` — for `FlexibleIconSource` / `FlexibleIcon` thumb icons

