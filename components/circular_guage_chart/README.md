# circular_gauge_chart

Animated circular progress gauge for Jetpack Compose.

Draws a background circle, a stroked progress arc that sweeps from 0% to the
current ratio when the value changes, and an inner disc that frames the
centered `"now / total"` label, an optional subtitle, and a percentage readout.

## Install

```kotlin
implementation("com.github.<user>.PalsUI:circular_gauge_chart:<tag>")
```

## Usage

```kotlin
CircularGaugeChart(
    nowValue = 75,
    totalValue = 150,
    title = "Steps",
)
```

With a custom spec:

```kotlin
CircularGaugeChart(
    nowValue = stepsToday,
    totalValue = stepsGoal,
    title = "Steps",
    spec = CircularGaugeChartDefaults.defaultSpec().copy(
        strokeWidth = 28.dp,
        animationDurationMs = 800,
        decimalPlaces = 0, // "50%" instead of "50.00%"
    ),
)
```

## Public API

| Type | Kind | Annotation |
|---|---|---|
| `CircularGaugeChart` | `@Composable` | — |
| `CircularGaugeChartSpec` | `data class` | `@Immutable` |
| `CircularGaugeChartColors` | `data class` | `@Immutable` |
| `CircularGaugeChartTypography` | `data class` | `@Immutable` |
| `CircularGaugeChartDefaults` | `object` | — |

## Notes

- The gauge fills the size granted by `modifier` when the parent supplies
  bounded constraints; otherwise it falls back to
  `CircularGaugeChartDefaults.FallbackSize` (240.dp). Radius is derived from
  `min(width, height) / 2`.
- `CircularGaugeChartDefaults.defaultSpec()` / `defaultColors()` /
  `defaultTypography()` are `@Composable` because they read `MaterialTheme`.
  Construct `CircularGaugeChartSpec` directly for a fully static default.
- `spec.decimalPlaces` controls percentage precision (default `2`).
- `contentDescription` is exposed for localization; `null` uses an English
  default.
- Throws `IllegalArgumentException` when `totalValue <= 0` or
  `spec.decimalPlaces < 0`.

See `docs/components/CircularGaugeChart.md` for the full reference.

