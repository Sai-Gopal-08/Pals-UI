# AnimatedText

## Overview

`AnimatedText` is a Jetpack Compose component that reveals text character-by-character with configurable animation effects. It supports multiple animation styles and correctly handles grapheme clusters — including emojis and multibyte Unicode — using `BreakIterator` internally.

---

## Module

```
:components:animated_text
```

| Property | Value |
|---|---|
| Namespace | `com.gopal.animated_text` |
| Min SDK | 24 |
| Type | Android Library |

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `AnimatedText` | `@Composable` | — | Entry point composable |
| `AnimatedTextSpec` | `data class` | `@Immutable` | Animation and text configuration |
| `AnimatedTextStyle` | `sealed class` | `@Stable` | Available animation styles |
| `AnimatedTextTransitionStyle` | `enum` | — | Transition physics for `JumpIn` |
| `AnimatedTextDefaults` | `object` | — | Pre-built default spec |

### AnimatedText

```kotlin
@Composable
fun AnimatedText(
    text: String,
    spec: AnimatedTextSpec,
    modifier: Modifier = Modifier
)
```

### AnimatedTextSpec

```kotlin
@Immutable
data class AnimatedTextSpec(
    val animationDurationForEachCharacter: Int,   // ms, must be ≥ 0
    val animationDelayForEachCharacter: Long,      // ms, must be ≥ 50
    val animationStyle: AnimatedTextStyle,
    val textStyle: TextStyle
)
```

### AnimatedTextStyle

| Subtype | Description |
|---|---|
| `ScaleIn` | Grows from 0% to 100% |
| `ScaleOut` | Shrinks from 140% to 100% |
| `FadeIn` | Fades from transparent to opaque |
| `JumpIn(animationTransitionStyle)` | Slides up from below — smooth or spring |
| `RotateIn` | Rotates from 45° tilt to upright using spring physics |

### AnimatedTextTransitionStyle

| Value | Behaviour |
|---|---|
| `SMOOTH` | Linear tween — consistent, no overshoot |
| `SPRING` | Spring physics — bouncy, natural settling |

---

## Default Values

`AnimatedTextDefaults.Default` is a static `val` — allocated once, zero allocations during recomposition.

| Property | Default |
|---|---|
| `animationDurationForEachCharacter` | `300` ms |
| `animationDelayForEachCharacter` | `100` ms |
| `animationStyle` | `AnimatedTextStyle.ScaleIn` |
| `textStyle` | `Color.Black`, `16sp`, `FontWeight.SemiBold`, `2sp` letter spacing |

---

## Usage Examples

### Basic — use defaults

```kotlin
AnimatedText(
    text = "Hello, World!",
    spec = AnimatedTextDefaults.Default
)
```

### Custom spec from scratch

```kotlin
AnimatedText(
    text = "Hello, World!",
    spec = AnimatedTextSpec(
        animationDurationForEachCharacter = 400,
        animationDelayForEachCharacter = 80,
        animationStyle = AnimatedTextStyle.FadeIn,
        textStyle = TextStyle(
            color = Color.DarkGray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    )
)
```

### Override a single property from defaults

```kotlin
AnimatedText(
    text = "Hello, World!",
    spec = AnimatedTextDefaults.Default.copy(
        animationStyle = AnimatedTextStyle.RotateIn
    )
)
```

### JumpIn with spring physics

```kotlin
AnimatedText(
    text = "Bouncy!",
    spec = AnimatedTextDefaults.Default.copy(
        animationStyle = AnimatedTextStyle.JumpIn(
            animationTransitionStyle = AnimatedTextTransitionStyle.SPRING
        )
    )
)
```

---

## Architecture Decisions

### Why flat file structure?

`AnimatedText` is a small, focused component. A flat structure keeps navigation minimal and avoids premature abstraction. Folders would only be introduced if the component grows significantly in complexity.

### Why `@Immutable` on `AnimatedTextSpec`?

`AnimatedTextSpec` is pure configuration — no mutable state, no business logic, no callbacks. `@Immutable` lets the Compose compiler skip unnecessary recompositions when the spec hasn't changed.

### Why `@Stable` on `AnimatedTextStyle`?

`AnimatedTextStyle` is a sealed class. Marking it `@Stable` signals to the Compose compiler that its subtypes have stable identity, preventing unnecessary recompositions when the style is used as a parameter.

### Why is `AnimatedTextDefaults.Default` a plain `val`?

A `@Composable` getter would create a new `AnimatedTextSpec` instance on every recomposition, causing avoidable allocations. A plain `val` on the object is allocated once at class load time and reused freely.

### Why `BreakIterator`?

Standard string indexing splits multibyte characters (emojis, accented letters, etc.) incorrectly. `BreakIterator` operates on grapheme clusters — the smallest user-perceived characters — ensuring each emoji or complex glyph animates as a single unit.

---

## File Structure

```
components/animated_text/
├── src/main/java/com/gopal/animated_text/
│   ├── AnimatedText.kt                  # Public composable + internal rendering
│   ├── AnimatedTextSpec.kt              # @Immutable configuration data class
│   ├── AnimatedTextDefaults.kt          # Pre-built default spec
│   ├── AnimatedTextStyle.kt             # @Stable sealed class of animation styles
│   └── AnimatedTextTransitionStyle.kt   # Enum — SMOOTH / SPRING
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
- [x] Naming follows library conventions (`AnimatedText` prefix on all public types)
- [x] Spec exists and is `@Immutable`
- [x] Spec contains configuration only
- [x] No State needed (state is managed internally)
- [x] No Callbacks needed
- [x] No unnecessary recompositions
- [x] `@Stable` / `@Immutable` annotations reviewed
- [x] No avoidable allocations during recomposition
- [x] Flat structure used
- [x] No generic model/common/data folders
- [ ] Public API reviewed for Maven publication
- [ ] Component ready for Maven publication

