package com.gopal.pill_tab_row

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private typealias TabStartPosition = Dp
private typealias TabWidth = Dp

/**
 * A pill-shaped tab row with an animated sliding indicator.
 *
 * @param onTabSelected Callback invoked with the index of the tapped tab.
 * @param selectedIndex Zero-based index of the currently selected tab.
 * @param spec Full appearance and behavior configuration.
 * @param modifier Modifier applied to the pill container.
 */
@Composable
fun PillTabRow(
    onTabSelected: (Int) -> Unit,
    selectedIndex: Int,
    spec: PillTabRowSpec,
    modifier: Modifier = Modifier,
) {
    val sectionCount = spec.titles.size
    require(sectionCount > 1) { "PillTabRow requires at least 2 sections" }

    val density = LocalDensity.current
    val tabPositions = remember { mutableStateListOf<Pair<TabStartPosition, TabWidth>>() }

    // Animate indicator position and width
    val indicatorStart by animateDpAsState(
        targetValue = tabPositions.getOrNull(selectedIndex)?.first ?: PillTabRowDefaults.fallBackDpValue,
        animationSpec = spec.indicatorAnimationSpec, label = "indicatorStart"
    )
    val indicatorWidth by animateDpAsState(
        targetValue = tabPositions.getOrNull(selectedIndex)?.second ?: PillTabRowDefaults.fallBackDpValue,
        animationSpec = spec.indicatorAnimationSpec, label = "indicatorWidth"
    )

    // Find the tab index the indicator is currently over using its center
    val indicatorCenter by remember { derivedStateOf { indicatorStart + indicatorWidth / 2 } }
    val currentTabIndex by remember(sectionCount, selectedIndex) {
        derivedStateOf {
            if (isTabLayoutStable(tabPositions.size, sectionCount)) {
                tabPositions.indexOfFirst { pair ->
                    val tabStart = pair.first
                    val tabEnd = tabStart + pair.second
                    indicatorCenter in tabStart..<tabEnd
                }.let { if (it == -1) selectedIndex else it }
            } else {
                selectedIndex
            }
        }
    }
    val indicatorShape by remember(sectionCount) {
        derivedStateOf {
            if (isTabLayoutStable(tabPositions.size, sectionCount)) {
                getShapeBasedOnIndex(
                    isFirstElement = (currentTabIndex == 0),
                    isLastElement = (currentTabIndex == sectionCount - 1),
                    spec = spec
                )
            } else {
                PillTabRowDefaults.middlePillShape
            }
        }
    }

    Box(
        modifier = modifier
            .height(spec.pillHeight)
            .clip(RoundedCornerShape(spec.pillCornerRadius))
            .background(spec.colors.backgroundColor)
    ) {
        // Draw indicator first so titles are above it
        if (tabPositions.size == sectionCount) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorStart)
                    .width(indicatorWidth)
                    .fillMaxHeight()
                    .clip(indicatorShape)
                    .background(spec.colors.indicatorColor)
            )
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            spec.titles.forEachIndexed { index, title ->
                val tabShape = getShapeBasedOnIndex(
                    isFirstElement = (index == 0),
                    isLastElement = (index == sectionCount - 1),
                    spec = spec
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(shape = tabShape)
                        .drawBordersAroundEachPill(
                            pillBorderSpec = spec.borderSpec,
                            isFirstElement = (index == 0),
                            isLastElement = (index == sectionCount - 1),
                            pillCornerRadius = spec.pillCornerRadius
                        )
                        .clickable { onTabSelected(index) }
                        .onGloballyPositioned { coordinates: LayoutCoordinates ->
                            val parent = coordinates.parentLayoutCoordinates
                            val startPx = if (parent != null) {
                                coordinates.positionInRoot().x - parent.positionInRoot().x
                            } else {
                                coordinates.positionInRoot().x
                            }
                            val widthPx = coordinates.size.width.toFloat()
                            val startDp = with(density) { startPx.toDp() }
                            val widthDp = with(density) { widthPx.toDp() }
                            val newPair = Pair(startDp, widthDp)
                            if (tabPositions.size < sectionCount) {
                                if (tabPositions.size > index) {
                                    if (tabPositions[index] != newPair) tabPositions[index] = newPair
                                } else {
                                    tabPositions.add(newPair)
                                }
                            } else {
                                if (tabPositions[index] != newPair) tabPositions[index] = newPair
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val isSelected = selectedIndex == index
                    Text(
                        text = title,
                        color = if (isSelected) spec.colors.selectedTextColor else spec.colors.textColor,
                        fontSize = spec.pillTitleFontSize,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = PillTabRowDefaults.minimumSpacingAroundPillTitle)
                    )
                }
            }
        }
    }
}

/**
 * Returns true when all tab positions have been measured —
 * i.e. the layout is ready for indicator animation.
 */
private fun isTabLayoutStable(tabPositions: Int, totalTabs: Int): Boolean =
    tabPositions == totalTabs

/**
 * Returns the correct clipping [Shape] for a tab based on its position in the row.
 * First tab gets rounded left corners; last tab gets rounded right corners;
 * middle tabs are square.
 */
private fun getShapeBasedOnIndex(isFirstElement: Boolean, isLastElement: Boolean, spec: PillTabRowSpec): Shape =
    when {
        isFirstElement -> PillTabRowDefaults.firstPillShape(spec.pillCornerRadius)
        isLastElement -> PillTabRowDefaults.lastPillShape(spec.pillCornerRadius)
        else -> PillTabRowDefaults.middlePillShape
    }

private fun Modifier.drawBordersAroundEachPill(
    pillBorderSpec: PillBorderSpec,
    isFirstElement: Boolean,
    isLastElement: Boolean,
    pillCornerRadius: Dp
): Modifier {
    return this.drawWithCache {
        val borderWidthPx = pillBorderSpec.borderWidth.toPx()
        val color = pillBorderSpec.borderColor
        val radius = pillCornerRadius.toPx()
        val strokeStyle = androidx.compose.ui.graphics.drawscope.Stroke(borderWidthPx)

        onDrawBehind {
            // Top border
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = borderWidthPx
            )
            // Bottom border
            drawLine(
                color = color,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = borderWidthPx
            )

            if (isFirstElement) {
                drawLine(
                    color = color,
                    start = Offset(0f, radius),
                    end = Offset(0f, size.height - radius),
                    strokeWidth = borderWidthPx
                )
                drawArc(
                    color = color,
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(0f, 0f),
                    size = androidx.compose.ui.geometry.Size(2 * radius, 2 * radius),
                    style = strokeStyle
                )
                drawArc(
                    color = color,
                    startAngle = 90f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(0f, size.height - 2 * radius),
                    size = androidx.compose.ui.geometry.Size(2 * radius, 2 * radius),
                    style = strokeStyle
                )
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderWidthPx
                )
            } else if (isLastElement) {
                drawLine(
                    color = color,
                    start = Offset(size.width, radius),
                    end = Offset(size.width, size.height - radius),
                    strokeWidth = borderWidthPx
                )
                drawArc(
                    color = color,
                    startAngle = 270f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(size.width - 2 * radius, 0f),
                    size = androidx.compose.ui.geometry.Size(2 * radius, 2 * radius),
                    style = strokeStyle
                )
                drawArc(
                    color = color,
                    startAngle = 0f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(size.width - 2 * radius, size.height - 2 * radius),
                    size = androidx.compose.ui.geometry.Size(2 * radius, 2 * radius),
                    style = strokeStyle
                )
            } else {
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderWidthPx
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoPillTabRowPrev() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        DemoPillTabRow()
    }
}

@Composable
private fun DemoPillTabRow() {
    val (selected, onChange) = remember { mutableIntStateOf(0) }
    PillTabRow(
        selectedIndex = selected,
        onTabSelected = onChange,
        spec = PillTabRowDefaults.defaultPillTabRowSpec,
    )
}
