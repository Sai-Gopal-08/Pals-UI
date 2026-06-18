# EdgeAccentCard

## Overview

A Material3 card composable with customisable per-edge accent decorations.

Edges can be styled with solid colors or 21-point symmetric gradients. Left and right edges
optionally include corner arcs. Partial edge spans allow drawing only a fraction of any edge.
All drawing is done via `drawWithCache` — no unnecessary recompositions from draw-level changes.

**Module:** `:components:edge_accent_card`  
**Package:** `com.gopal.edge_accent_card`  
**Min SDK:** 24

---

## Public API

### `EdgeAccentCard`

```kotlin
@Composable
fun EdgeAccentCard(
    cardConfig: CardConfig,
    edgeDecoration: EdgeDecoration,
    modifier: Modifier = Modifier,
    cardContent: @Composable () -> Unit,
)
```

---

### `CardConfig` (`@Immutable`)

```kotlin
@Immutable
data class CardConfig(
    val cornerRadius: Dp,
    val cardColors: CardColors,
    val cardBorder: BorderStroke? = null,
    val cardElevation: CardElevation,
)
```

---

### `EdgeDecoration` (`@Immutable`)

```kotlin
@Immutable
data class EdgeDecoration(
    val edges: List<Edge>,
    val style: EdgeStyle,
    val span: EdgeSpan? = null,  // null = full edge
)
```

---

### `EdgeStyle` (`@Stable`)

```kotlin
@Stable
sealed class EdgeStyle {
    data class Solid(val color: Color, val strokeWidth: Dp) : EdgeStyle()
    data class Gradient(
        val color: Color, val strokeWidth: Dp,
        val centerAlpha: Float = 1f, val edgeAlpha: Float = 0f
    ) : EdgeStyle()
}
```

---

### `Edge` (`@Stable`)

```kotlin
@Stable
sealed class Edge {
    sealed class Left : Edge() {
        data class WithCorners(topSweepAngle, bottomSweepAngle, arcStrokeWidthMultiplier?) : Left()
        data object WithoutCorners : Left()
    }
    sealed class Right : Edge() {
        data class WithCorners(topSweepAngle, bottomSweepAngle, arcStrokeWidthMultiplier?) : Right()
        data object WithoutCorners : Right()
    }
    data object Top : Edge()
    data object Bottom : Edge()
}
```

---

### `EdgeSpan` (`@Immutable`)

```kotlin
@Immutable
data class EdgeSpan(val startFraction: Float, val endFraction: Float)
```

Fractions are in `0f → 1f`. `startFraction` must be ≤ `endFraction`.

---

### `EdgeAccentCardDefaults`

| Member | Type | Detail |
|---|---|---|
| `DefaultCornerRadius` | `Dp` | `16.dp` — static singleton |
| `DefaultStrokeWidth` | `Dp` | `4.dp` — static singleton |
| `FullEdge` | `EdgeSpan` | `0.0 → 1.0` — static singleton |
| `CenteredHalfEdge` | `EdgeSpan` | `0.25 → 0.75` — static singleton |
| `LeadingHalfEdge` | `EdgeSpan` | `0.0 → 0.5` — static singleton |
| `TrailingHalfEdge` | `EdgeSpan` | `0.5 → 1.0` — static singleton |
| `cardConfig(...)` | `@Composable` fun | Material3-themed `CardConfig` |
| `solidEdge(...)` | fun | `EdgeStyle.Solid` factory |
| `gradientEdge(...)` | fun | `EdgeStyle.Gradient` factory |
| `leftEdgeWithCorners(...)` | fun | `Edge.Left.WithCorners` factory |
| `rightEdgeWithCorners(...)` | fun | `Edge.Right.WithCorners` factory |
| `leftEdgeDecoration(...)` | fun | Preset: solid left edge |
| `rightEdgeDecoration(...)` | fun | Preset: solid right edge |
| `horizontalEdgesDecoration(...)` | fun | Preset: gradient top + bottom |
| `allEdgesDecoration(...)` | fun | Preset: all edges |

---

## Usage Examples

### Left edge with corners

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeAccentCardDefaults.leftEdgeDecoration(color = Color.Blue)
) { Text("Content", modifier = Modifier.padding(24.dp)) }
```

### Gradient horizontal edges

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeAccentCardDefaults.horizontalEdgesDecoration(
        color = Color.Red, centerAlpha = 1f, edgeAlpha = 0f
    )
) { /* content */ }
```

### All edges

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(cornerRadius = 20.dp),
    edgeDecoration = EdgeAccentCardDefaults.allEdgesDecoration(color = Color.Magenta)
) { /* content */ }
```

### Partial left edge (no corners)

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

### Custom corner sweep angles

```kotlin
EdgeAccentCard(
    cardConfig = EdgeAccentCardDefaults.cardConfig(),
    edgeDecoration = EdgeAccentCardDefaults.edgeDecoration(
        edges = listOf(EdgeAccentCardDefaults.leftEdgeWithCorners(topSweepAngle = 30f, bottomSweepAngle = 60f)),
        style = EdgeAccentCardDefaults.solidEdge(Color.Blue)
    )
) { /* content */ }
```

---

## Architecture Decisions

### Why `@Stable` on `Edge` and `EdgeStyle` sealed classes?
Sealed classes are not inferred as stable by the Compose compiler unless annotated. Without `@Stable`,
passing `Edge` or `EdgeStyle` as a parameter would cause `EdgeAccentCard` to be considered unstable,
forcing recomposition on every parent recomposition.

### Why `@Immutable` on `EdgeSpan`?
`EdgeSpan` is a `data class` — the Compose compiler cannot automatically infer `@Immutable` on it
unless explicitly annotated. `@Immutable` enables structural equality checks to skip recompositions.

### Why was `model/` folder removed?
The ADR explicitly lists `model/` as a **rejected** generic folder pattern. All model types are now
at the flat package level — consistent with `AnimatedText`, `ContextMenuCard`, and `StyledSwitch`.

### Why was `EdgeSpanDefaults` removed?
`EdgeSpanDefaults` duplicated all 4 span presets already present in `EdgeStyledCardDefaults`.
The consolidated `EdgeAccentCardDefaults` is the single source of truth for all defaults.

### Why `drawWithCache`?
`drawWithCache` runs only when the composable's **size changes**, not on every recomposition.
The `Path`, `CornerRadius`, and gradient stop arrays are cached across recompositions — preventing
expensive allocations during scroll or state changes.

---

## Edge Span Behaviour

⚠️ `EdgeDecoration.span` applies only to:
- `Edge.Top` / `Edge.Bottom` — controls horizontal fraction
- `Edge.Left.WithoutCorners` / `Edge.Right.WithoutCorners` — controls vertical fraction

**Ignored** for `Edge.Left.WithCorners` and `Edge.Right.WithCorners` — those always draw full height including corner arcs.

---

## File Structure

```
edge_accent_card/
├── EdgeAccentCard.kt          # Public composable + private draw functions
├── EdgeAccentCardDefaults.kt  # All defaults, presets, factory methods
├── CardConfig.kt              # @Immutable card config
├── EdgeDecoration.kt          # @Immutable edge decoration config
├── EdgeStyle.kt               # @Stable sealed class — Solid / Gradient
├── Edge.kt                    # @Stable sealed class — Left / Right / Top / Bottom
├── EdgeSpan.kt                # @Immutable fractional span
└── README.md
```

---

## Validation Checklist

### Module
- [x] Component has its own module
- [x] README exists — `components/edge_accent_card/README.md`
- [x] Component documentation added to `docs/components/`
- [ ] Sample/demo exists in showcase app

### API Design
- [x] Public API is minimal
- [x] Implementation details hidden — all draw functions are `private`
- [x] Naming follows library conventions — `EdgeAccentCard` matches module name

### Spec
- [x] Spec exists — `CardConfig` + `EdgeDecoration`
- [x] Spec is immutable — `@Immutable` on both
- [x] Spec contains configuration only

### State
- [x] State exists only if needed — fully stateless

### Callbacks
- [x] Single `cardContent` slot — correct

### Compose Performance
- [x] All composable parameters are stable — `@Stable` on `Edge`, `EdgeStyle`; `@Immutable` on `CardConfig`, `EdgeDecoration`, `EdgeSpan`
- [x] No unnecessary recompositions — `drawWithCache` caches geometry per size change
- [x] Stable/Immutable annotations reviewed
- [x] No avoidable allocations — gradient stops computed in `drawWithCache`, not recomposition

### Structure
- [x] Flat structure — `model/` folder removed, all types at package root
- [x] No generic folders — `model/` eliminated, `defaults/` merged into flat `EdgeAccentCardDefaults`

### Publishing
- [ ] Public API reviewed
- [ ] Documentation completed
- [x] Flat structure used by default
- [ ] Additional folders justified by complexity — 5 spec types at flat level; `spec/` folder warranted per ADR Large Component Template

