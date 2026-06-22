package com.gopal.pill_tab_row

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PillTabRowDefaults {
    private val defaultPillHeight = 48.dp
    private val defaultPillCornerRadius = 24.dp
    private val defaultPillTitleFontSize = 16.sp
    private val defaultPillColors = PillTabRowColors(
        backgroundColor = Color.LightGray,
        indicatorColor = Color.Blue,
        textColor = Color.Black,
        selectedTextColor = Color.White
    )
    private val defaultIndicatorAnimationSpec: TweenSpec<Dp> = tween(durationMillis = 350, easing = FastOutSlowInEasing)
    private val titles = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4")
    private val pillBorderSpec = PillBorderSpec(
        borderColor = Color.Black,
        borderWidth = 2.dp
    )
    val defaultPillTabRowSpec = PillTabRowSpec(
        pillHeight = defaultPillHeight,
        pillCornerRadius = defaultPillCornerRadius,
        pillTitleFontSize = defaultPillTitleFontSize,
        colors = defaultPillColors,
        indicatorAnimationSpec = defaultIndicatorAnimationSpec,
        titles = titles,
        borderSpec = pillBorderSpec
    )

    fun firstPillShape(cornerRadius: Dp) = RoundedCornerShape(
        topStart = cornerRadius,
        bottomStart = cornerRadius,
        topEnd = 0.dp,
        bottomEnd = 0.dp
    )
    fun lastPillShape(cornerRadius: Dp) = RoundedCornerShape(
        topStart = 0.dp,
        bottomStart = 0.dp,
        topEnd = cornerRadius,
        bottomEnd = cornerRadius
    )
    val middlePillShape = RoundedCornerShape(0.dp)
    val fallBackDpValue = 0.dp
    val minimumSpacingAroundPillTitle = 4.dp
}