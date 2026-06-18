package com.gopal.styled_switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gopal.flexible_icon.FlexibleIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * A fully customizable animated toggle switch.
 *
 * Supports custom colors, shapes, elevation, border, and optional thumb icons via [StyledSwitchSpec].
 * Use [StyledSwitchDefaults.spec] for a Material3-themed default configuration.
 *
 * @param checked Whether the switch is currently checked (on).
 * @param onCheckedChange Callback invoked when the user toggles the switch.
 * @param modifier Modifier applied to the outer track container.
 * @param enabled Whether the switch responds to user interaction.
 * @param spec Full appearance and behavior configuration. Defaults to [StyledSwitchDefaults.spec].
 */
@Composable
fun StyledSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    spec: StyledSwitchSpec = StyledSwitchDefaults.spec(),
) {
    val animationDuration = spec.animationDuration
    val elevation = spec.elevation
    val borderWidth = spec.borderSpec.borderWidth
    val borderColor = spec.borderSpec.borderColor
    val trackShape = spec.trackShape
    val colors = spec.colors
    val switchWidth = spec.thumbSize * 2
    val switchHeight = spec.thumbSize + 4.dp
    val thumbShape = CircleShape

    val trackColor by animateColorAsState(
        targetValue = if (checked) colors.checkedTrackColor else colors.uncheckedTrackColor,
        animationSpec = tween(durationMillis = animationDuration),
        label = "TrackColor",
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) colors.checkedThumbColor else colors.uncheckedThumbColor,
        animationSpec = tween(durationMillis = animationDuration),
        label = "ThumbColor",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) spec.thumbSize else 0.dp,
        animationSpec = tween(durationMillis = animationDuration),
        label = "ThumbOffset",
    )

    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .clip(trackShape)
            .background(trackColor)
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(spec.thumbSize)
                .shadow(elevation, thumbShape)
                .clip(thumbShape)
                .background(thumbColor)
                .border(width = borderWidth, color = borderColor, shape = thumbShape),
            contentAlignment = Alignment.Center,
        ) {
            spec.icons?.let { icons ->
                FlexibleIcon(
                    source = if (checked) icons.checkedIcon else icons.uncheckedIcon,
                    modifier = Modifier.size(spec.thumbSize),
                    tint = colors.iconTint,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StyledSwitchPreview() {
    var isChecked by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        StyledSwitch(
            checked = isChecked,
            onCheckedChange = {
                scope.launch {
                    isChecked = it
                    isAnimating = true
                    delay(900L.milliseconds)
                    isAnimating = false
                }
            },
            enabled = !isAnimating,
        )
    }
}

