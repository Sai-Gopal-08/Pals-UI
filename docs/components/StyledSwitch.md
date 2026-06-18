# StyledSwitch

## Overview

A fully customisable animated toggle switch built with Jetpack Compose.

Handles all animation internally (`animateColorAsState`, `animateDpAsState`) and exposes a single
`StyledSwitchSpec` for complete appearance control. Thumb icons are supported via `FlexibleIconSource`,
allowing vectors, drawable resources, bitmap resources, or raw bitmaps.

**Module:** `:components:styled_switch`  
**Package:** `com.gopal.styled_switch`  
**Min SDK:** 24  
**Depends on:** `:components:flexible_icon`

---

## Public API

### `StyledSwitch`

```kotlin
@Composable
fun StyledSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    spec: StyledSwitchSpec = StyledSwitchDefaults.spec(),
)
```

| Parameter | Type | Detail |
|---|---|---|
| `checked` | `Boolean` | Whether the switch is on |
| `onCheckedChange` | `(Boolean) -> Unit` | Called when user toggles |
| `modifier` | `Modifier` | Applied to the outer track |
| `enabled` | `Boolean` | Enables/disables interaction |
| `spec` | `StyledSwitchSpec` | Full appearance config |

---

### `StyledSwitchSpec` (`@Immutable`)

```kotlin
@Immutable
data class StyledSwitchSpec(
    val animationDuration: Int,
    val elevation: Dp,
    val thumbSize: Dp,
    val trackShape: Shape,
    val borderSpec: StyledSwitchBorderSpec,
    val icons: StyledSwitchIcons?,
    val colors: StyledSwitchColors,
)
```

---

### `StyledSwitchBorderSpec` (`@Immutable`)

```kotlin
@Immutable
data class StyledSwitchBorderSpec(
    val borderColor: Color,
    val borderWidth: Dp,
)
```

---

### `StyledSwitchIcons` (`@Immutable`)

```kotlin
@Immutable
data class StyledSwitchIcons(
    val checkedIcon: FlexibleIconSource,
    val uncheckedIcon: FlexibleIconSource,
)
```

Accepts any `FlexibleIconSource` subtype — vector, drawable resource, bitmap resource, or raw bitmap.

---

### `StyledSwitchColors` (`@Immutable`)

```kotlin
@Immutable
data class StyledSwitchColors(
    val checkedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedTrackColor: Color,
    val uncheckedThumbColor: Color,
    val iconTint: Color,
)
```

---

### `StyledSwitchDefaults`

| Member | Returns | Detail |
|---|---|---|
| `thumbSize` | `Dp` | `32.dp` — static singleton, zero allocation |
| `elevation` | `Dp` | `4.dp` — static singleton |
| `trackShape` | `Shape` | `CircleShape` — static singleton |
| `borderSpec` | `StyledSwitchBorderSpec` | Transparent border, 0dp width |
| `icons` | `StyledSwitchIcons?` | `null` — no thumb icons by default |
| `colors()` | `@Composable StyledSwitchColors` | Reads `MaterialTheme.colorScheme` |
| `spec()` | `@Composable StyledSwitchSpec` | Full default spec |

---

## Usage Examples

### Basic

```kotlin
var checked by remember { mutableStateOf(false) }

StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it }
)
```

### Custom colors

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        colors = StyledSwitchColors(
            checkedTrackColor = Color(0xFF4CAF50),
            uncheckedTrackColor = Color(0xFFBDBDBD),
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color.White,
            iconTint = Color.White
        )
    )
)
```

### Custom shape and size

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

### With border

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

### With thumb icons (vector)

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        icons = StyledSwitchIcons(
            checkedIcon = FlexibleIconSource.VectorIcon(
                imageVector = Icons.Default.Check,
                contentDescriptionResource = R.string.on
            ),
            uncheckedIcon = FlexibleIconSource.VectorIcon(
                imageVector = Icons.Default.Close,
                contentDescriptionResource = R.string.off
            )
        )
    )
)
```

### With thumb icons (drawable resource)

```kotlin
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    spec = StyledSwitchDefaults.spec().copy(
        icons = StyledSwitchIcons(
            checkedIcon = FlexibleIconSource.DrawableResourceIcon(
                drawableRes = R.drawable.ic_check,
                contentDescriptionResource = R.string.on
            ),
            uncheckedIcon = FlexibleIconSource.DrawableResourceIcon(
                drawableRes = R.drawable.ic_close,
                contentDescriptionResource = R.string.off
            )
        )
    )
)
```

---

## Architecture Decisions

### Why `FlexibleIconSource` for icons?
Using `ImageVector` would limit callers to vector drawables only. `FlexibleIconSource` gives full
flexibility — the same interface used by `FlexibleIcon` — with no redundant icon rendering code.

### Why `@Immutable` on all spec types?
The Compose compiler uses `@Immutable` to skip recomposition of `StyledSwitch` when the spec
reference changes but values are structurally equal. Without it, any parent recomposition would
retrigger `StyledSwitch` even with identical configuration.

### Why `@Composable fun colors()` instead of a property?
Colors depend on `MaterialTheme.colorScheme` which is only accessible in a composable scope.
The function pattern mirrors Material3's own `ButtonDefaults.buttonColors()`, `CardDefaults.cardColors()` etc.
`StyledSwitchColors` is `@Immutable`, so downstream composables skip recomposition when colors are equal.

### Why `remember(animationDuration)` for tween specs?
`tween(...)` creates a new `AnimationSpec` object. Without `remember`, three new objects would
allocate on every recomposition triggered by `checked` changes. Keyed on `animationDuration` — the
spec is recomputed only when the duration actually changes.

### Why is state fully hoisted?
`StyledSwitch` is stateless — `checked` and `onCheckedChange` are provided by the caller.
This is the correct Compose state hoisting pattern and allows the component to be used in any
state management setup (ViewModel, `remember`, etc.) without internal assumptions.

---

## File Structure

```
styled_switch/
├── StyledSwitch.kt          # Public composable
├── StyledSwitchSpec.kt      # StyledSwitchSpec, StyledSwitchBorderSpec,
│                            # StyledSwitchIcons, StyledSwitchColors
├── StyledSwitchDefaults.kt  # Default values
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
- [x] Public API is minimal
- [x] Implementation details are hidden
- [x] Naming follows library conventions

### Spec
- [x] Spec exists when configuration is required
- [x] Spec is immutable
- [x] Spec contains configuration only

### State
- [x] State exists only if needed — stateless, fully hoisted
- [x] State is properly hoisted

### Callbacks
- [x] Small callback count uses direct lambdas (`onCheckedChange`)

### Compose Performance
- [x] All composable parameters are stable — `StyledSwitchSpec` is `@Immutable`
- [x] No unnecessary recompositions — animations key on `checked` only
- [x] Stable/Immutable annotations reviewed — all 4 spec types annotated
- [x] No avoidable allocations — `tween` specs wrapped in `remember(animationDuration)`

### Structure
- [x] Flat structure used by default
- [x] No generic model/common/data folders

### Publishing
- [ ] Public API reviewed
- [ ] Documentation completed
- [ ] Component ready for Maven publication

