package com.gopal.context_menu_card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

private typealias UniqueKeySpecificToCard = String

/**
 * Displays a customizable card with contextual menu support that appears at the long-press location.
 *
 * This composable combines a Material3 [Card] with gesture detection and a [DropdownMenu] that appears
 * precisely where the user long-presses on the card. The menu visibility is **automatically managed**
 * internally, showing on long press and hiding on dismiss. The card animates with a subtle scale effect
 * and elevation change when the menu is displayed.
 *
 * Key features:
 * - **Automatic menu management**: Menu shows on long press and hides on dismiss without external state
 * - Long-press gesture detection with menu positioning at touch point
 * - Animated scale (1.0 to 1.02) and elevation changes when menu is visible
 * - Configurable card appearance through [ContextMenuCardSpec]
 * - Optional callbacks for additional developer actions
 *
 * @param spec Specification defining the card's appearance, including shape, colors, elevation,
 * border, and animation behavior. Also provides the unique key for gesture scoping.
 * @param modifier Optional [Modifier] for customizing the card's layout and styling.
 * @param state State object controlling if clicks are enabled. Defaults to enabled.
 * @param onCardClick Optional callback invoked when the card is tapped, receiving the card's unique identifier.
 * Only triggered if [ContextMenuCardState.areClicksEnabled] is true.
 * @param onLongPress Optional callback invoked when the card is long-pressed (after menu is shown),
 * receiving the card's unique identifier. Use this for additional side effects like analytics or haptics.
 * Only triggered if [ContextMenuCardState.areClicksEnabled] is true.
 * @param onMenuDismissed Optional callback invoked when the dropdown menu is dismissed (after menu is hidden).
 * Use this for additional cleanup or state updates.
 * @param cardContent Composable lambda with [BoxScope] receiver for defining the card's content.
 * @param dropDownColumnContent Composable lambda with [ColumnScope] receiver for defining the dropdown menu items.
 * Typically contains [DropdownMenuItem] composables.
 *
 * @sample ContextMenuCardPreview
 */
@Composable
fun ContextMenuCard(
    spec: ContextMenuCardSpec,
    modifier: Modifier = Modifier,
    state: ContextMenuCardState = ContextMenuCardState(),
    onCardClick: ((UniqueKeySpecificToCard) -> Unit)? = null,
    onLongPress: ((UniqueKeySpecificToCard) -> Unit)? = null,
    onMenuDismissed: (() -> Unit)? = null,
    cardContent: @Composable BoxScope.() -> Unit,
    dropDownColumnContent: @Composable ColumnScope.() -> Unit
) {
    // Internal state for menu visibility - managed automatically
    var shouldShowMenu by remember { mutableStateOf(false) }
    var longPressOffset by remember { mutableStateOf(Offset.Zero) }

    val hoveredScale by animateFloatAsState(
        targetValue = if (shouldShowMenu) 1.02f else 1f,
        label = "hoveredScale",
        animationSpec = spec.cardHoverAnimationSpec
            ?: ContextMenuCardDefaults.cardHoverAnimationSpec
    )

    Card(
        shape = spec.cardShape
            ?: ContextMenuCardDefaults.cardShape,
        colors = spec.cardColors
            ?: ContextMenuCardDefaults.cardColors,
        elevation = if (shouldShowMenu) {
            spec.hoveredCardElevation
                ?: ContextMenuCardDefaults.hoveredCardElevation
        } else {
            spec.defaultCardElevation
                ?: ContextMenuCardDefaults.defaultCardElevation
        },
        border = spec.cardBorderStroke,
        modifier = modifier
            .graphicsLayer {
                scaleX = hoveredScale
                scaleY = hoveredScale
            }
            .pointerInput(spec.uniqueKeySpecificToCard) {
                detectTapGestures(
                    onLongPress = { offset ->
                        if (state.areClicksEnabled) {
                            longPressOffset = offset
                            // Auto-show menu
                            shouldShowMenu = true
                            // Invoke optional callback for side effects
                            onLongPress?.invoke(spec.uniqueKeySpecificToCard)
                        }
                    },
                    onTap = {
                        if (state.areClicksEnabled) {
                            onCardClick?.invoke(spec.uniqueKeySpecificToCard)
                        }
                    }
                )
            }
    ) {
        Box {
            cardContent()
            Box {
                DropdownMenu(
                    expanded = shouldShowMenu,
                    onDismissRequest = {
                        // Auto-hide menu
                        shouldShowMenu = false
                        // Invoke optional callback for cleanup
                        onMenuDismissed?.invoke()
                    },
                    offset = DpOffset(
                        x = convertPxToDp(px = longPressOffset.x),
                        y = convertPxToDp(longPressOffset.y)
                    ),
                ) {
                    dropDownColumnContent()
                }
            }
        }
    }
}

@Composable
private fun convertPxToDp(px: Float): Dp {
    return with(LocalDensity.current) { px.toDp() }
}

@Preview(showBackground = true)
@Composable
private fun ContextMenuCardPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ContextMenuCard(
            modifier = Modifier.height(200.dp),
            spec = ContextMenuCardSpec(
                uniqueKeySpecificToCard = "sample_card"
            ),
            onCardClick = { cardId ->
                println("Card clicked: $cardId")
            },
            onLongPress = { cardId ->
                println("Card long-pressed: $cardId")
            },
            onMenuDismissed = {
                println("Menu dismissed")
            },
            cardContent = {
                Text(
                    text = "Long press on this card to see the context menu",
                    modifier = Modifier.padding(16.dp)
                )
            },
            dropDownColumnContent = {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = { /* Handle edit */ }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { /* Handle delete */ }
                )
                DropdownMenuItem(
                    text = { Text("Share") },
                    onClick = { /* Handle share */ }
                )
            }
        )
    }
}

