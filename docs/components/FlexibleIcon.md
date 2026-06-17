# FlexibleIcon

## Overview

`FlexibleIcon` is a Jetpack Compose component that renders an icon from any supported asset source through a single unified API. Callers choose the source type via `FlexibleIconSource` — vector drawable, drawable resource, bitmap resource, or raw bitmap — and the component handles the correct `Icon(...)` overload internally.

---

## Module

```
:components:flexible_icon
```

| Property | Value |
|---|---|
| Namespace | `com.gopal.flexible_icon` |
| Min SDK | 24 |
| Type | Android Library |

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `FlexibleIcon` | `@Composable` | — | Entry point composable |
| `FlexibleIconSource` | `sealed interface` | `@Stable` | Discriminated union of supported icon source types |

### FlexibleIcon

```kotlin
@Composable
fun FlexibleIcon(
    source: FlexibleIconSource,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
)
```

### FlexibleIconSource

```kotlin
@Stable
sealed interface FlexibleIconSource {
    val contentDescriptionResource: @StringRes Int

    data class VectorIcon(
        val imageVector: ImageVector,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource

    data class DrawableResourceIcon(
        @DrawableRes val drawableRes: Int,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource

    data class BitmapResourceIcon(
        @DrawableRes val bitmapRes: Int,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource

    data class BitmapIcon(
        val bitmapData: ImageBitmap,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource
}
```

---

## Source Types

| Subtype | When to use |
|---|---|
| `VectorIcon` | Material icons or any `ImageVector` |
| `DrawableResourceIcon` | PNG, XML, or any drawable resource via `@DrawableRes` |
| `BitmapResourceIcon` | Bitmap drawable resources decoded at runtime via `@DrawableRes` |
| `BitmapIcon` | Dynamically loaded or generated `ImageBitmap` |

---

## Usage Examples

### Vector icon

```kotlin
FlexibleIcon(
    source = FlexibleIconSource.VectorIcon(
        imageVector = Icons.Default.Info,
        contentDescriptionResource = R.string.icon_description
    )
)
```

### Drawable resource

```kotlin
FlexibleIcon(
    source = FlexibleIconSource.DrawableResourceIcon(
        drawableRes = R.drawable.my_icon,
        contentDescriptionResource = R.string.icon_description
    )
)
```

### Bitmap resource

```kotlin
FlexibleIcon(
    source = FlexibleIconSource.BitmapResourceIcon(
        bitmapRes = R.drawable.my_bitmap,
        contentDescriptionResource = R.string.icon_description
    )
)
```

### Raw bitmap

```kotlin
FlexibleIcon(
    source = FlexibleIconSource.BitmapIcon(
        bitmapData = myImageBitmap,
        contentDescriptionResource = R.string.icon_description
    )
)
```

### With tint

```kotlin
FlexibleIcon(
    source = FlexibleIconSource.VectorIcon(
        imageVector = Icons.Default.Star,
        contentDescriptionResource = R.string.icon_description
    ),
    tint = MaterialTheme.colorScheme.primary
)
```

---

## Architecture Decisions

### Why no Spec?

`FlexibleIcon` has only three parameters: `source`, `modifier`, and `tint`. A Spec wrapper would add boilerplate without clarity benefit. Per the ADR, Specs are introduced only when configuration is genuinely complex.

### Why no State?

The component is fully stateless — it renders exactly what it receives. No internal mutable state, no state hoisting needed.

### Why `@Stable` on `FlexibleIconSource`?

`FlexibleIconSource` is a `sealed interface`. The Compose compiler cannot automatically infer stability for interfaces — only for classes and data classes. Without `@Stable`, the compiler treats `FlexibleIconSource` as unstable, which prevents `FlexibleIcon` from being skipped during recomposition even when inputs haven't changed. Marking it `@Stable` restores skipping behaviour. All subtypes are `data class`es with stable properties, so the contract is upheld.

### Why `remember(resId)` in bitmap loading?

Decoding a bitmap from resources is an expensive operation. `remember(resId)` caches the decoded `ImageBitmap` keyed on the resource ID — it is only decoded once per unique resource, not on every recomposition.

---

## File Structure

```
components/flexible_icon/
├── src/main/java/com/gopal/flexible_icon/
│   ├── FlexibleIcon.kt          # Public composable + private bitmap loader
│   └── FlexibleIconSource.kt    # @Stable sealed interface of icon source types
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
- [x] Implementation details are hidden (`loadImageBitmapFromRes` is `private`)
- [x] Naming follows library conventions (`FlexibleIcon` prefix on all public types)
- [x] No Spec needed (3 parameters, no complex configuration)
- [x] No State needed (fully stateless component)
- [x] No Callbacks needed
- [x] All composable parameters are stable types — UI recomposes only when inputs genuinely change
- [x] No unnecessary recompositions
- [x] `@Stable` annotation on `FlexibleIconSource` reviewed and applied
- [x] No avoidable allocations — bitmap decoding cached via `remember(resId)`
- [x] Flat structure used
- [x] No generic model/common/data folders
- [ ] Public API reviewed for Maven publication
- [ ] Component ready for Maven publication

