# AdaptiveStyledText

## Overview

`AdaptiveStyledText` is a Jetpack Compose component that renders text composed of multiple inline-styled segments — color, weight, style, decoration — as a single `Text` instance. It produces a Material `Text` backed by an `AnnotatedString` built from the supplied segments, preserving line-wrap and overflow behavior across the whole content.

---

## Module

```
:components:adaptive_styled_text
```

| Property | Value |
|---|---|
| Namespace | `com.gopal.adaptive_styled_text` |
| Min SDK | 24 |
| Type | Android Library |

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `AdaptiveStyledText` | `@Composable` | — | Entry point composable |
| `AdaptiveStyledTextSpec` | `data class` | `@Immutable` | Segments + text-layout configuration |
| `AdaptiveStyledTextSegment` | `data class` | `@Immutable` | A single inline-styled run of text |
| `AdaptiveStyledTextDefaults` | `object` | — | Convenience builders for common segment styles |

### AdaptiveStyledText

```kotlin
@Composable
fun AdaptiveStyledText(
    spec: AdaptiveStyledTextSpec,
    modifier: Modifier = Modifier
)
```

### AdaptiveStyledTextSpec

```kotlin
@Immutable
data class AdaptiveStyledTextSpec(
    val segments: List<AdaptiveStyledTextSegment>,
    val baseTextStyle: TextStyle = TextStyle.Default,
    val overflow: TextOverflow = TextOverflow.Clip,
    val softWrap: Boolean = true,
    val maxLines: Int = Int.MAX_VALUE,
    val minLines: Int = 1,
    val textAlign: TextAlign? = null
)
```

### AdaptiveStyledTextSegment

```kotlin
@Immutable
data class AdaptiveStyledTextSegment(
    val text: String,
    val color: Color = Color.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val textDecoration: TextDecoration? = null
)
```

Unspecified segment properties (`Color.Unspecified`, `null` weight/style/decoration) fall through to `AdaptiveStyledTextSpec.baseTextStyle`.

### AdaptiveStyledTextDefaults

| Function | Returns segment with |
|---|---|
| `segment(text, color)` | No inline overrides |
| `boldSegment(text, color)` | `FontWeight.Bold` |
| `italicSegment(text, color)` | `FontStyle.Italic` |
| `underlinedSegment(text, color)` | `TextDecoration.Underline` |
| `strikethroughSegment(text, color)` | `TextDecoration.LineThrough` |
| `highlightedSegment(text, color)` | `FontWeight.SemiBold` |

---

## Default Values

`AdaptiveStyledTextSpec`'s primary constructor exposes every parameter with a sensible default, so there is no separate `Default` instance to allocate.

| Property | Default |
|---|---|
| `segments` | _required_ |
| `baseTextStyle` | `TextStyle.Default` |
| `overflow` | `TextOverflow.Clip` |
| `softWrap` | `true` |
| `maxLines` | `Int.MAX_VALUE` |
| `minLines` | `1` |
| `textAlign` | `null` (natural alignment) |

---

## Usage Examples

### Basic

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        segments = listOf(
            AdaptiveStyledTextSegment(text = "Hello ", color = Color.Black),
            AdaptiveStyledTextSegment(
                text = "World",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        )
    )
)
```

### With convenience builders

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        segments = listOf(
            AdaptiveStyledTextDefaults.segment("Regular "),
            AdaptiveStyledTextDefaults.boldSegment("Bold ", color = Color.Blue),
            AdaptiveStyledTextDefaults.italicSegment("Italic ", color = Color.Gray),
            AdaptiveStyledTextDefaults.underlinedSegment("Underlined", color = Color.Red)
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
)
```

### Custom base style

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        baseTextStyle = MaterialTheme.typography.bodyLarge,
        segments = listOf(
            AdaptiveStyledTextSegment("Welcome ", color = MaterialTheme.colorScheme.onSurface),
            AdaptiveStyledTextSegment(
                text = "PalsUI",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
    )
)
```

### Search-result highlighting

```kotlin
AdaptiveStyledText(
    spec = AdaptiveStyledTextSpec(
        segments = listOf(
            AdaptiveStyledTextDefaults.segment("Hello "),
            AdaptiveStyledTextDefaults.highlightedSegment("World", color = Color.Yellow),
            AdaptiveStyledTextDefaults.segment(", welcome.")
        )
    )
)
```

---

## Architecture Decisions

### Why flat file structure?

`AdaptiveStyledText` is a small, focused component (four public types). Per the PalsUI ADR, folders are introduced only when complexity justifies them; here a flat layout is the right default.

### Why `@Immutable` on `AdaptiveStyledTextSpec` and `AdaptiveStyledTextSegment`?

Both are pure-data `data class`es with `val` fields and no mutable state. `@Immutable` lets the Compose compiler treat them as fully stable and skip recomposition when the same instance (or a structurally-equal one) is passed across composes.

### Why `remember(spec)` around the `AnnotatedString` build?

`buildAnnotatedString { segments.forEach { … } }` allocates a new `AnnotatedString` and iterates every segment. Without memoization, this would run on every recomposition even when the spec hasn't changed. Wrapping it in `remember(spec)` makes the iteration run only when the spec instance changes by structural equality.

### Why no `Default` spec on `AdaptiveStyledTextDefaults`?

Unlike `AnimatedText`, the spec here is dominated by a caller-supplied `segments` list — there is no meaningful "empty default" to hand out. The data-class constructor already provides defaults for every other parameter, so a separate `Default` would only add API surface.

### Why convenience segment factories instead of a DSL?

`boldSegment`, `italicSegment`, etc. cover the common cases with one call each, while still letting callers drop down to the raw `AdaptiveStyledTextSegment(...)` constructor for full control. A DSL would add API surface without removing any of the underlying configuration knobs.

### What is **not** supported

- Interactive spans (clickable / linkable text). Build a custom `AnnotatedString` with `pushStringAnnotation` and use Material's `Text` directly if you need that.
- Paragraph-level styling (per-paragraph alignment, lists, block styles). This component is inline-only.

---

## File Structure

```
components/adaptive_styled_text/
├── src/main/java/com/gopal/adaptive_styled_text/
│   ├── AdaptiveStyledText.kt           # Public composable + internal AnnotatedString build
│   ├── AdaptiveStyledTextSpec.kt       # @Immutable configuration data class
│   ├── AdaptiveStyledTextSegment.kt    # @Immutable single styled run
│   └── AdaptiveStyledTextDefaults.kt   # Convenience segment factories
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
- [x] Implementation details are hidden
- [x] Naming follows library conventions (`AdaptiveStyledText` prefix on all public types)
- [x] Spec exists and is `@Immutable`
- [x] Spec contains configuration only
- [x] No State needed
- [x] No Callbacks needed
- [x] All composable parameters are stable types — UI recomposes only when inputs genuinely change
- [x] No unnecessary recompositions (`AnnotatedString` build wrapped in `remember(spec)`)
- [x] `@Stable` / `@Immutable` annotations reviewed
- [x] No avoidable allocations during recomposition
- [x] Flat structure used
- [x] No generic model/common/data folders
- [ ] Public API reviewed for Maven publication
- [ ] Component ready for Maven publication

