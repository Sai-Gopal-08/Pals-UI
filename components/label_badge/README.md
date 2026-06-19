# LabelBadge

A compact, customisable badge composable for Jetpack Compose that displays a text value
inside a styled container.

---

## Module

```
:components:label_badge
```

**Namespace:** `com.gopal.label_badge`  
**Min SDK:** 24

---

## Basic Usage

```kotlin
LabelBadge(value = "42")
```

---

## Rendering Styles

### Custom style (default)
Sizes the badge precisely to its text content using `TextMeasurer`.

```kotlin
LabelBadge(
    value = "99+",
    spec = LabelBadgeDefaults.spec().copy(style = LabelBadgeStyle.CUSTOM)
)
```

### Material3 Badge style
Delegates to Material3's `Badge` composable for standard Material badge sizing.

```kotlin
LabelBadge(
    value = "3",
    spec = LabelBadgeDefaults.spec().copy(style = LabelBadgeStyle.MATERIAL3_BADGE)
)
```

---

## Custom Appearance

```kotlin
LabelBadge(
    value = "NEW",
    spec = LabelBadgeSpec(
        containerColor = Color.Red,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        ),
        shape = RoundedCornerShape(4.dp),
        paddingAroundTextInDp = 6.dp,
        style = LabelBadgeStyle.CUSTOM,
    )
)
```

---

## Public API

| Type | Role |
|---|---|
| `LabelBadge` | Main composable |
| `LabelBadgeSpec` | Configuration (`@Immutable`) |
| `LabelBadgeStyle` | Rendering strategy enum (`@Stable`) |
| `LabelBadgeDefaults` | Material3-themed defaults |

### `LabelBadgeDefaults`

| Member | Type | Detail |
|---|---|---|
| `shape` | `Shape` | `RoundedCornerShape(8.dp)` — static singleton |
| `padding` | `Dp` | `4.dp` — static singleton |
| `spec()` | `@Composable` fun | Full spec using `MaterialTheme` colors |

