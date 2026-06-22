# AdaptiveStyledText

A Jetpack Compose text component for rendering a single line/paragraph composed of
multiple inline-styled segments — color, weight, style, decoration — without resorting
to multiple `Text` composables.

Part of the **PalsUI** component library.

## Why

Compose's `Text` accepts an `AnnotatedString`, but building one by hand is verbose. This
component lets you declare segments in a `data class` list and renders them as one piece
of text, preserving line-wrap and overflow behavior.

## Usage

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        segments = listOf(
            AdaptiveStyledTextSegment(text = "Hello ", color = Color.Black),
            AdaptiveStyledTextSegment(
                text = "World",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
            ),
        ),
    ),
)
```

### With the convenience builders

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        segments = listOf(
            AdaptiveStyledTextDefaults.segment("Regular "),
            AdaptiveStyledTextDefaults.boldSegment("Bold ", color = Color.Blue),
            AdaptiveStyledTextDefaults.italicSegment("Italic ", color = Color.Gray),
            AdaptiveStyledTextDefaults.underlinedSegment("Underlined", color = Color.Red),
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    ),
)
```

### Custom base style

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        baseTextStyle = MaterialTheme.typography.bodyLarge,
        segments = listOf(
            AdaptiveStyledTextSegment("Hello ", color = MaterialTheme.colorScheme.onSurface),
            AdaptiveStyledTextSegment(
                text = "PalsUI",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            ),
        ),
    ),
)
```

## API

### `AdaptiveStyledTextSegment`

| Property         | Type               | Default              |
|------------------|--------------------|----------------------|
| `text`           | `String`           | —                    |
| `color`          | `Color`            | `Color.Unspecified`  |
| `fontWeight`     | `FontWeight?`      | `null`               |
| `fontStyle`      | `FontStyle?`       | `null`               |
| `textDecoration` | `TextDecoration?`  | `null`               |

`Color.Unspecified` / `null` properties fall through to the base style.

### `AdaptiveStyledTextSpec`

| Property         | Type                              | Default                 |
|------------------|-----------------------------------|-------------------------|
| `segments`       | `List<AdaptiveStyledTextSegment>` | —                       |
| `baseTextStyle`  | `TextStyle`                       | `TextStyle.Default`     |
| `overflow`       | `TextOverflow`                    | `TextOverflow.Clip`     |
| `softWrap`       | `Boolean`                         | `true`                  |
| `maxLines`       | `Int`                             | `Int.MAX_VALUE`         |
| `minLines`       | `Int`                             | `1`                     |
| `textAlign`      | `TextAlign?`                      | `null`                  |

### `AdaptiveStyledTextDefaults`

Factory functions for common segment styles: `segment`, `boldSegment`, `italicSegment`,
`underlinedSegment`, `strikethroughSegment`, `highlightedSegment`.

## Performance

- `AdaptiveStyledTextSpec` and `AdaptiveStyledTextSegment` are `@Immutable` — the
  composable is skippable when the same spec is passed across recompositions.
- The resulting `AnnotatedString` is built inside `remember(spec)`, so the segment loop
  only runs when the spec instance changes (structural equality).

