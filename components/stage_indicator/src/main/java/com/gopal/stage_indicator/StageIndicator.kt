package com.gopal.stage_indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview

/**
 * Displays a row of indicator dots representing pages or stages.
 *
 * Dots at or before [currentIndex] are rendered in the filled color;
 * dots after it use the unfilled color. The width of each dot is calculated
 * dynamically to fill the available row width evenly.
 *
 * @param currentIndex Zero-based index of the current active page.
 * @param modifier Modifier applied to the indicator row.
 * @param spec Appearance configuration. Defaults to [StageIndicatorDefaults.spec].
 */
@Composable
fun StageIndicator(
    currentIndex: Int,
    modifier: Modifier = Modifier,
    spec: StageIndicatorSpec = StageIndicatorDefaults.spec(),
) {
    val totalNumberOfIndicators = spec.totalPages
    val spacingBetweenIndicators = spec.indicatorSpacing
    val shape = CircleShape
    val density = LocalDensity.current

    var indicatorWidth by remember { mutableIntStateOf(0) }
    val totalSpacing = spacingBetweenIndicators * (totalNumberOfIndicators - 1)

    Row(
        modifier = modifier
            .onGloballyPositioned {
                val maxWidth = it.size.width
                // Convert spacing from Dp to px for correct pixel-level arithmetic
                val spacingPx = with(density) { totalSpacing.toPx() }.toInt()
                val availableWidth = maxWidth - spacingPx
                indicatorWidth = availableWidth / totalNumberOfIndicators
            },
        horizontalArrangement = Arrangement.spacedBy(spacingBetweenIndicators),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalNumberOfIndicators) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = with(density) { indicatorWidth.toDp() },
                        height = spec.indicatorHeight,
                    )
                    .background(
                        color = if (index <= currentIndex) {
                            spec.stageIndicatorColors.filledIndicatorColor
                        } else {
                            spec.stageIndicatorColors.unfilledIndicatorColor
                        },
                        shape = shape,
                    )
                    .border(
                        width = spec.indicatorBorderSpec.borderWidth,
                        color = spec.indicatorBorderSpec.borderColor,
                        shape = shape,
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StageIndicatorPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        StageIndicator(
            currentIndex = 1,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
    }
}

