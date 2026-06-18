# EdgeAccentCard

A customisable Jetpack Compose card that renders per-edge accent decorations — solid colors, symmetric gradients, and optional corner arcs.

---

## Module

```
:components:edge_accent_card
```

**Namespace:** `com.gopal.edge_accent_card`  
**Min SDK:** 24

---

## Basic Usage

```kotlin
// Left edge with corners (using defaults)
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeAccentCardDefaults.leftEdgeDecoration(color = Color.Blue)
) {
    Text("My Content", modifier = Modifier.padding(24.dp))
}
```

---

## Usage Examples

### Solid left edge with corner arcs

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeAccentCardDefaults.leftEdgeDecoration(color = Color.Blue)
) { /* content */ }
```

### Gradient top + bottom edges

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeAccentCardDefaults.horizontalEdgesDecoration(
        color = Color.Red,
        centerAlpha = 1f,
        edgeAlpha = 0f
    )
) { /* content */ }
```

### All edges, custom corner radius

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(cornerRadius = 20.dp),
    edgeDecoration = EdgeAccentCardDefaults.allEdgesDecoration(color = Color.Magenta)
) { /* content */ }
```

### Partial edge span (left, no corners)

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeDecoration(
        edges = listOf(Edge.Left.WithoutCorners),
        style = EdgeStyle.Solid(color = Color.Cyan, strokeWidth = 5.dp),
        span = EdgeSpan(startFraction = 0.2f, endFraction = 0.8f)
    )
) { /* content */ }
```

### Manual configuration

```kotlin
EdgeAccentCard(
    cardConfig = CardConfig(
        cornerRadius = 16.dp,
        cardColors = CardDefaults.cardColors(),
        cardElevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ),
    edgeDecoration = EdgeDecoration(
        edges = listOf(Edge.Left.WithCorners(topSweepAngle = 45f, bottomSweepAngle = 45f)),
        style = EdgeStyle.Solid(color = Color.Blue, strokeWidth = 4.dp)
    )
) { /* content */ }
```

---

## Public API

| Type | Role |
|---|---|
| `EdgeAccentCard` | Main composable |
| `CardConfig` | Card shape, colors, border, elevation (`@Immutable`) |
| `EdgeDecoration` | Which edges to draw and how (`@Immutable`) |
| `EdgeStyle` | `Solid` or `Gradient` edge style (`@Stable`) |
| `Edge` | `Left`, `Right`, `Top`, `Bottom` with/without corners (`@Stable`) |
| `EdgeSpan` | Fractional start/end position for partial edges (`@Immutable`) |
| `EdgeAccentCardDefaults` | Default values, presets, and factory methods |

---

## Edge Span Behaviour

⚠️ `EdgeDecoration.span` applies only to:
- `Edge.Top` / `Edge.Bottom` — horizontal fraction
- `Edge.Left.WithoutCorners` / `Edge.Right.WithoutCorners` — vertical fraction

`Edge.Left.WithCorners` and `Edge.Right.WithCorners` **always ignore span** — they draw the full edge including corner arcs.

