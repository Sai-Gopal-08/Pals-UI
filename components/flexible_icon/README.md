# FlexibleIcon

A Jetpack Compose component that renders an icon from any supported asset source — vector drawable, drawable resource, bitmap resource, or raw bitmap — through a single unified API.

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `FlexibleIcon` | `@Composable` | — | The component |
| `FlexibleIconSource` | `sealed interface` | `@Stable` | Defines the icon source type |

---

## Basic Usage

### Vector icon (from ImageVector)

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

## FlexibleIconSource Subtypes

| Subtype | Input | Use case |
|---|---|---|
| `VectorIcon` | `ImageVector` | Material icons, XML vector drawables loaded as vectors |
| `DrawableResourceIcon` | `@DrawableRes Int` | Any drawable resource (PNG, XML, etc.) |
| `BitmapResourceIcon` | `@DrawableRes Int` | Bitmap drawable resources decoded at runtime |
| `BitmapIcon` | `ImageBitmap` | Dynamically loaded or generated bitmaps |

---

## Module

```
:components:flexible_icon
```

Namespace: `com.gopal.flexible_icon`

