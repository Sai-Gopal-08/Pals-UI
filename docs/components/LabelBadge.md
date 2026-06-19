# LabelBadge

## Overview

A compact, customisable badge composable that displays a text value inside a shaped container.

Supports two rendering strategies:
- **Custom** — measures text at composition time and sizes the badge precisely to fit.
- **Material3 Badge** — delegates to Material3's `Badge` composable for standard styling.

**Module:** `:components:label_badge`  
**Package:** `com.gopal.label_badge`  
**Min SDK:** 24

---

## Public API

### `LabelBadge`

```kotlin
@Composable
fun LabelBadge(
    value: String,
    modifier: Modifier = Modifier,
    spec: LabelBadgeSpec = LabelBadgeDefaults.spec(),
)
```

| Parameter | Type | Detail |
|---|---|---|
| `value` | `String` | Text displayed inside the badge |
| `modifier` | `Modifier` | Applied to the badge container |
| `spec` | `LabelBadgeSpec` | Full appearance configuration |

---

### `LabelBadgeSpec` (`@Immutable`)

```kotlin
@Immutable
data class LabelBadgeSpec(
    val containerColor: Color,
    val textStyle: TextStyle,
    val shape: Shape,
    val paddingAroundTextInDp: Dp,
    val style: LabelBadgeStyle = LabelBadgeStyle.CUSTOM,
)
```

---

### `LabelBadgeStyle` (`@Stable`)

```kotlin
@Stable
enum class LabelBadgeStyle {
    CUSTOM,          // Sizes badge precisely to text content via TextMeasurer
    MATERIAL3_BADGE, // Delegates to Material3 Badge composable
}
```

---

### `LabelBadgeDefaults`

| Member | Returns | Detail |
|---|---|---|
| `shape` | `Shape` | `RoundedCornerShape(8.dp)` — static singleton |
| `padding` | `Dp` | `4.dp` — static singleton |
| `spec()` | `@Composable LabelBadgeSpec` | Full spec from `MaterialTheme` |

---

## Usage Examples

### Basic

```kotlin
LabelBadge(value = "42")
```

### Material3 style

```kotlin
LabelBadge(
    value = "3",
    spec = LabelBadgeDefaults.spec().copy(style = LabelBadgeStyle.MATERIAL3_BADGE)
)
```

### Custom colors and shape

```kotlin
LabelBadge(
    value = "NEW",
    spec = LabelBadgeSpec(
        containerColor = Color.Red,
        textStyle = TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold),
        shape = RoundedCornerShape(4.dp),
        paddingAroundTextInDp = 6.dp,
    )
)
```

### Override only style

```kotlin
LabelBadge(
    value = "99+",
    spec = LabelBadgeDefaults.spec().copy(style = LabelBadgeStyle.CUSTOM)
)
```

---

## Architecture Decisions

### Why `@Immutable` on `LabelBadgeSpec`?
Without `@Immutable`, the Compose compiler treats `LabelBadgeSpec` as potentially unstable
(due to the `Shape` interface field), causing `LabelBadge` to recompose on every parent recomposition.
`@Immutable` tells the compiler to trust structural equality, so recomposition only triggers when
values genuinely change.

### Why `@Stable` on `LabelBadgeStyle` enum?
Enums are technically stable types, but explicit `@Stable` makes the intent clear to contributors
and ensures the annotation is preserved even if the type is ever changed to a sealed class.

### Why `remember(value, textStyle)` around `textMeasurer.measure(...)`?
`TextMeasurer.measure()` creates a new `TextLayoutResult` object on each call. Without `remember`,
every recomposition — even unrelated ones — would re-measure the text and allocate a new layout result.
Keying on `value` and `textStyle` ensures measurement only re-runs when those inputs actually change.

### Why `@Composable fun spec()` instead of a getter?
Follows the established convention across all PalsUI components (`ContextMenuCard`, `StyledSwitch`,
`EdgeAccentCard`). A `@Composable` function is explicit about requiring a composition scope and
mirrors Material3's own `ButtonDefaults.buttonColors()` pattern. A `@Composable` getter is an
unusual Kotlin construct that surprises readers.

---

## File Structure

```
label_badge/
├── LabelBadge.kt          # Public composable + private rendering variants
├── LabelBadgeSpec.kt      # @Immutable configuration
├── LabelBadgeStyle.kt     # @Stable rendering strategy enum
├── LabelBadgeDefaults.kt  # Default values
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
- [x] Public API is minimal — only `LabelBadge()` is public
- [x] Implementation details hidden — rendering variants are `private`
- [x] Naming follows library conventions — `LabelBadge*` matches module `label_badge`

### Spec
- [x] Spec exists — `LabelBadgeSpec`
- [x] Spec is immutable — `@Immutable`
- [x] Spec contains configuration only

### State
- [x] State exists only if needed — fully stateless

### Callbacks
- [x] No callbacks needed

### Compose Performance
- [x] All composable parameters are stable — `@Immutable` on `LabelBadgeSpec`, `@Stable` on `LabelBadgeStyle`
- [x] No unnecessary recompositions
- [x] Stable/Immutable annotations reviewed
- [x] No avoidable allocations — `textMeasurer.measure()` wrapped in `remember(value, textStyle)`

### Structure
- [x] Flat structure used by default
- [x] Additional folders justified by complexity — N/A (4 files, flat is appropriate)
- [x] No generic model/common/data folders

### Publishing
- [ ] Public API reviewed
- [ ] Documentation completed
- [ ] Component ready for Maven publication

