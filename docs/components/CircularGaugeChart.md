# CircularGaugeChart

## Overview

`CircularGaugeChart` is a Jetpack Compose component that renders an animated
circular progress gauge. It draws a background circle, a stroked progress arc
that sweeps from 0% to the current ratio whenever the value changes, and an
inner disc framing a centered `"now / total"` label, an optional subtitle, and
a percentage readout.

---

## Module

```
:components:circular_gauge_chart
```

| Property | Value |
|---|---|
| Namespace | `com.gopal.circular_gauge_chart` |
| Min SDK | 24 |
| Type | Android Library |

---

## Public API

| Name | Kind | Annotation | Purpose |
|---|---|---|---|
| `CircularGaugeChart` | `@Composable` | — | Entry-point composable |
| `CircularGaugeChartSpec` | `data class` | `@Immutable` | Animation + visual configuration |
| `CircularGaugeChartColors` | `data class` | `@Immutable` | Color palette |
| `CircularGaugeChartTypography` | `data class` | `@Immutable` | Label text styles |
| `CircularGaugeChartDefaults` | `object` | — | Theme-aware default factories + `FallbackSize` |

### CircularGaugeChart

```kotlin
@Composable
fun CircularGaugeChart(
    nowValue: Int,
    totalValue: Int,
    modifier: Modifier = Modifier,
    title: String = "",
    contentDescription: String? = null,
    spec: CircularGaugeChartSpec = CircularGaugeChartDefaults.defaultSpec(),
)
```

- Requires `totalValue > 0` and `spec.decimalPlaces >= 0` (throws
  `IllegalArgumentException` otherwise). Validation runs once per distinct
  `(totalValue, decimalPlaces)`, not on every recomposition.
- `nowValue` is clamped into `0..totalValue` when computing the displayed sweep.
- **Sizing:** when `modifier` supplies bounded constraints in both axes, the
  gauge fills them; when the parent is unbounded (e.g. inside a vertical scroll
  with no `Modifier.size`), it falls back to
  `CircularGaugeChartDefaults.FallbackSize`. Radius is always derived from
  `min(width, height) / 2`.
- `contentDescription` is applied to the whole gauge for screen readers. Pass a
  localized string, or `null` to use the built-in English default
  (`"Progress: 75 of 150, 50.00%"`).

### CircularGaugeChartSpec

```kotlin
@Immutable
data class CircularGaugeChartSpec(
    val animationDurationMs: Int,
    val strokeWidth: Dp,
    val innerDiscInset: Dp,
    val decimalPlaces: Int,
    val colors: CircularGaugeChartColors,
    val typography: CircularGaugeChartTypography,
)
```

`decimalPlaces` controls the precision of the percentage label: `0` → `"50%"`,
`2` → `"49.95%"`.

### CircularGaugeChartColors

```kotlin
@Immutable
data class CircularGaugeChartColors(
    val backgroundCircle: Color,
    val innerDisc: Color,
    val progress: Color,
    val primaryLabel: Color,
    val secondaryLabel: Color,
)
```

### CircularGaugeChartTypography

```kotlin
@Immutable
data class CircularGaugeChartTypography(
    val primaryLabel: TextStyle,
    val secondaryLabel: TextStyle,
    val percentageLabel: TextStyle,
)
```

### CircularGaugeChartDefaults

```kotlin
object CircularGaugeChartDefaults {
    val FallbackSize: Dp                                 // 240.dp
    @Composable fun defaultSpec(): CircularGaugeChartSpec
    @Composable fun defaultColors(): CircularGaugeChartColors
    @Composable fun defaultTypography(): CircularGaugeChartTypography
}
```

`defaultSpec`, `defaultColors` and `defaultTypography` read `MaterialTheme`, so
the gauge integrates with the host app's theme automatically. For a fully
static, non-themed default, construct `CircularGaugeChartSpec` directly.

---

## Default Values

| Property | Default |
|---|---|
| `animationDurationMs` | `1200` ms |
| `strokeWidth` | `20.dp` |
| `innerDiscInset` | `24.dp` |
| `decimalPlaces` | `2` |
| `colors.backgroundCircle` | `MaterialTheme.colorScheme.surfaceVariant` |
| `colors.innerDisc` | `MaterialTheme.colorScheme.surface` |
| `colors.progress` | `MaterialTheme.colorScheme.primary` |
| `colors.primaryLabel` | `MaterialTheme.colorScheme.onSurface` |
| `colors.secondaryLabel` | `MaterialTheme.colorScheme.onSurfaceVariant` |
| `typography.primaryLabel` | `MaterialTheme.typography.titleLarge` |
| `typography.secondaryLabel` | `MaterialTheme.typography.bodyLarge` |
| `typography.percentageLabel` | `MaterialTheme.typography.bodyLarge` |
| `FallbackSize` | `240.dp` |

---

## Usage Examples

### Basic — use defaults

```kotlin
CircularGaugeChart(
    nowValue = 75,
    totalValue = 150,
    title = "Progress",
)
```

### Custom stroke + duration

```kotlin
CircularGaugeChart(
    nowValue = stepsToday,
    totalValue = stepsGoal,
    title = "Steps",
    spec = CircularGaugeChartDefaults.defaultSpec().copy(
        strokeWidth = 28.dp,
        animationDurationMs = 800,
    ),
)
```

### Ring-only (no inner disc)

```kotlin
CircularGaugeChart(
    nowValue = 60,
    totalValue = 100,
    spec = CircularGaugeChartDefaults.defaultSpec().copy(
        innerDiscInset = 0.dp,
    ),
)
```

### No subtitle

```kotlin
CircularGaugeChart(nowValue = 42, totalValue = 100) // title defaults to ""
```

### Integer percentage (no decimals)

```kotlin
CircularGaugeChart(
    nowValue = 50,
    totalValue = 100,
    spec = CircularGaugeChartDefaults.defaultSpec().copy(
        decimalPlaces = 0, // renders "50%" instead of "50.00%"
    ),
)
```

### Localized / custom accessibility label

```kotlin
CircularGaugeChart(
    nowValue = stepsToday,
    totalValue = stepsGoal,
    title = stringResource(R.string.steps),
    contentDescription = stringResource(
        R.string.steps_progress_a11y, stepsToday, stepsGoal,
    ),
)
```

---

## Architecture Decisions

### Why flat file structure?

Three files (`CircularGaugeChart`, `CircularGaugeChartSpec`, `CircularGaugeChartDefaults`)
fit the small-component template from the architecture spec. No folders.

### Why `@Immutable` on spec + colors?

Both are pure config data classes with `val` fields and no mutable state. The
annotation lets the Compose compiler skip recomposition when the spec is
structurally unchanged.

### Why is `defaultSpec()` `@Composable`?

It reads `MaterialTheme.colorScheme`, which is only available in composition
scope. A `@Composable fun` (rather than a `@Composable val get()`) is the
convention used across PalsUI for theme-dependent defaults — explicit about
requiring composition scope, mirrors Material3's `ButtonDefaults.buttonColors()`.

### Why is `title` a parameter, not a spec field?

The title is content, not configuration. Keeping it as a parameter avoids
reallocating the entire spec when the title changes, and matches Compose
convention (content on the composable, config on the spec).

### Why drop `outerRadius` / `innerRadius` from the spec?

Size belongs to the modifier. Hard-coding radii in the spec made the
`modifier` parameter decorative and broke `Modifier.size(...)` overrides.
Now the gauge fills the available space and derives its radius from it.

### Why `nowValue` + `totalValue` instead of `percentage: Float`?

Previously the composable took both, and the caller had to keep them
consistent. Now there is one source of truth for the sweep angle and the
label: the percentage is computed internally from the ratio.

### Why is sizing driven by `BoxWithConstraints`?

`Canvas` needs a concrete size. Hard-pinning it to a fixed `Modifier.size(...)`
ignored any caller-supplied `modifier` size; unconditionally using
`fillMaxSize()` crashes inside unbounded parents (vertical scroll, `wrapContent`)
because the canvas would measure to infinity. `BoxWithConstraints` inspects
`constraints.hasBoundedWidth/Height`: bounded → `fillMaxSize()` (respect the
caller), unbounded → fall back to `FallbackSize`. This mirrors how Material's
own size-sensitive components behave.

### Why is `decimalPlaces` on the spec?

It's a visual-formatting choice that travels with the rest of the gauge's
appearance, so it belongs with the other config. Defaults to `2`. Callers who
want locale-specific number formatting can pre-format and pass a custom
`contentDescription`; the on-screen label uses `String.format` with the
configured precision.

### Why a single `contentDescription` and no animation a11y?

The gauge communicates a static fact ("X of Y, Z%"). One concise description
conveys that to screen readers. The sweep animation is decorative — announcing
intermediate animated values would be noise, so it is intentionally not exposed
to accessibility services.

---

## File Structure

```
components/circular_gauge_chart/
├── src/main/java/com/gopal/circular_gauge_chart/
│   ├── CircularGaugeChart.kt
│   ├── CircularGaugeChartSpec.kt
│   └── CircularGaugeChartDefaults.kt
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
- [x] Naming follows library conventions
- [x] Spec exists and is `@Immutable`
- [x] Spec contains configuration only
- [x] No State needed
- [x] No Callbacks needed
- [x] All composable parameters are stable types
- [x] No unnecessary recompositions
- [x] `@Stable` / `@Immutable` annotations reviewed
- [x] No avoidable allocations during recomposition
- [x] Flat structure used
- [x] No generic model/common/data folders
- [ ] Public API reviewed for Maven publication
- [ ] Component ready for Maven publication

