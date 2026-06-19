package com.gopal.label_badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview

/**
 * Displays a small badge with a text value.
 *
 * Supports two rendering strategies via [LabelBadgeSpec.style]:
 * - [LabelBadgeStyle.CUSTOM] — sizes the badge precisely to its text content.
 * - [LabelBadgeStyle.MATERIAL3_BADGE] — delegates to Material3's `Badge` composable.
 *
 * @param value Text value displayed inside the badge.
 * @param modifier Modifier applied to the badge container.
 * @param spec Appearance configuration. Defaults to [LabelBadgeDefaults.spec].
 */
@Composable
fun LabelBadge(
    value: String,
    modifier: Modifier = Modifier,
    spec: LabelBadgeSpec = LabelBadgeDefaults.spec(),
) {
    when (spec.style) {
        LabelBadgeStyle.MATERIAL3_BADGE -> LabelBadgeMaterial3Style(
            value = value,
            modifier = modifier,
            spec = spec,
        )
        LabelBadgeStyle.CUSTOM -> LabelBadgeCustomStyle(
            value = value,
            modifier = modifier,
            spec = spec,
        )
    }
}

@Composable
private fun LabelBadgeMaterial3Style(
    value: String,
    modifier: Modifier = Modifier,
    spec: LabelBadgeSpec = LabelBadgeDefaults.spec(),
) {
    Badge(
        modifier = modifier
            .clip(spec.shape)
            .background(color = spec.containerColor, shape = spec.shape),
        containerColor = spec.containerColor,
    ) {
        Text(
            modifier = Modifier.padding(vertical = spec.paddingAroundTextInDp),
            text = value,
            style = spec.textStyle,
        )
    }
}

@Composable
private fun LabelBadgeCustomStyle(
    value: String,
    modifier: Modifier = Modifier,
    spec: LabelBadgeSpec = LabelBadgeDefaults.spec(),
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // Cached per value + textStyle — avoids allocating TextLayoutResult on every recomposition
    val textLayoutResult = remember(value, spec.textStyle) {
        textMeasurer.measure(text = value, style = spec.textStyle)
    }

    val boxWidth = with(density) { textLayoutResult.size.width.toDp() } + spec.paddingAroundTextInDp * 2
    val boxHeight = with(density) { textLayoutResult.size.height.toDp() } + spec.paddingAroundTextInDp * 2

    Box(
        modifier = modifier
            .size(width = boxWidth, height = boxHeight)
            .clip(spec.shape)
            .background(color = spec.containerColor, shape = spec.shape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value,
            style = spec.textStyle,
            modifier = Modifier.padding(spec.paddingAroundTextInDp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LabelBadgeCustomStylePreview() {
    LabelBadge(value = "42")
}

@Preview(showBackground = true)
@Composable
private fun LabelBadgeMaterial3StylePreview() {
    LabelBadge(
        value = "42",
        spec = LabelBadgeDefaults.spec().copy(style = LabelBadgeStyle.MATERIAL3_BADGE),
    )
}

