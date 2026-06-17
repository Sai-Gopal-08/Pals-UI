# AnimatedText

A Jetpack Compose component that reveals text character-by-character with configurable animation effects.

Uses `BreakIterator` internally to correctly handle grapheme clusters — including emojis and multibyte Unicode characters.

---

## Public API

| Name | Type | Purpose |
|---|---|---|
| `AnimatedText` | `@Composable` | The component |
| `AnimatedTextSpec` | `@Immutable data class` | Animation and text configuration |
| `AnimatedTextStyle` | `@Stable sealed class` | Available animation styles |
| `AnimatedTextTransitionStyle` | `enum` | Transition physics for `JumpIn` |
| `AnimatedTextDefaults` | `object` | Pre-built default spec |

---

## Basic Usage

```kotlin
AnimatedText(
    text = "Hello, World!",
    spec = AnimatedTextDefaults.Default
)
```

---

## Custom Spec

```kotlin
AnimatedText(
    text = "Hello, World!",
    spec = AnimatedTextSpec(
        animationDurationForEachCharacter = 300,
        animationDelayForEachCharacter = 100,
        animationStyle = AnimatedTextStyle.ScaleIn,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    )
)
```

---

## Customize from Defaults

```kotlin
AnimatedText(
    text = "Hello, World!",
    spec = AnimatedTextDefaults.Default.copy(
        animationStyle = AnimatedTextStyle.RotateIn
    )
)
```

---

## Animation Styles

| Style | Description |
|---|---|
| `AnimatedTextStyle.ScaleIn` | Characters grow from 0% to 100% size |
| `AnimatedTextStyle.ScaleOut` | Characters shrink from 140% to 100% size |
| `AnimatedTextStyle.FadeIn` | Characters fade in from transparent to opaque |
| `AnimatedTextStyle.JumpIn` | Characters slide up from below with optional spring physics |
| `AnimatedTextStyle.RotateIn` | Characters rotate from 45° tilt to upright using spring physics |

### JumpIn with Spring

```kotlin
AnimatedText(
    text = "Bouncy Text",
    spec = AnimatedTextDefaults.Default.copy(
        animationStyle = AnimatedTextStyle.JumpIn(
            animationTransitionStyle = AnimatedTextTransitionStyle.SPRING
        )
    )
)
```

---

## AnimatedTextSpec Parameters

| Parameter | Type | Description | Constraint |
|---|---|---|---|
| `animationDurationForEachCharacter` | `Int` | Duration (ms) of each character's animation | ≥ 0 |
| `animationDelayForEachCharacter` | `Long` | Delay (ms) between each character appearing | ≥ 50 |
| `animationStyle` | `AnimatedTextStyle` | The animation effect to apply | — |
| `textStyle` | `TextStyle` | Font, size, color, and weight of the text | — |

---

## Module

```
:components:animated_text
```

Namespace: `com.gopal.animated_text`

