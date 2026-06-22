package com.gopal.adaptive_styled_text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

/**
 * Renders text composed of multiple inline-styled [AdaptiveStyledTextSegment]s.
 *
 * Each segment inherits [AdaptiveStyledTextSpec.baseTextStyle] and may override
 * color, weight, style, or decoration.
 *
 * @param spec Full configuration: segments + text-layout controls.
 * @param modifier Modifier applied to the underlying `Text`.
 */
@Composable
fun AdaptiveStyledText(
    spec: AdaptiveStyledTextSpec,
    modifier: Modifier = Modifier,
) {
    val annotated = remember(spec) { buildAnnotatedStringFromSegments(spec) }
    Text(
        text = annotated,
        style = spec.baseTextStyle,
        overflow = spec.overflow,
        softWrap = spec.softWrap,
        maxLines = spec.maxLines,
        minLines = spec.minLines,
        textAlign = spec.textAlign,
        modifier = modifier,
    )
}

/**
 * Builds an [AnnotatedString] by merging each [AdaptiveStyledTextSegment]'s overrides
 * over [AdaptiveStyledTextSpec.baseTextStyle].
 */
private fun buildAnnotatedStringFromSegments(
    spec: AdaptiveStyledTextSpec,
): AnnotatedString {
    val baseStyle = spec.baseTextStyle
    return buildAnnotatedString {
        spec.segments.forEach { segment ->
            withStyle(
                style = baseStyle.copy(
                    color = if (segment.color != Color.Unspecified) segment.color else baseStyle.color,
                    fontWeight = segment.fontWeight ?: baseStyle.fontWeight,
                    fontStyle = segment.fontStyle ?: baseStyle.fontStyle,
                    textDecoration = segment.textDecoration ?: baseStyle.textDecoration,
                ).toSpanStyle()
            ) {
                append(segment.text)
            }
        }
    }
}

@Preview(showBackground = true, name = "Basic")
@Composable
private fun AdaptiveStyledTextBasicPreview() {
    AdaptiveStyledText(
        spec = AdaptiveStyledTextSpec(
            segments = listOf(
                AdaptiveStyledTextSegment(text = "Hello ", color = Color.Black),
                AdaptiveStyledTextSegment(
                    text = "World",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                ),
            ),
        ),
    )
}

@Preview(showBackground = true, name = "Complex")
@Composable
private fun AdaptiveStyledTextComplexPreview() {
    AdaptiveStyledText(
        spec = AdaptiveStyledTextSpec(
            segments = listOf(
                AdaptiveStyledTextSegment("Welcome ", Color.Gray),
                AdaptiveStyledTextSegment("to ", Color.Black),
                AdaptiveStyledTextSegment(
                    text = "Adaptive",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                ),
                AdaptiveStyledTextSegment(" Styled ", Color.Black),
                AdaptiveStyledTextSegment(
                    text = "Text",
                    color = Color(0xFF006400),
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                ),
            ),
            maxLines = 2,
        ),
    )
}

@Preview(showBackground = true, name = "Using Defaults")
@Composable
private fun AdaptiveStyledTextDefaultsPreview() {
    AdaptiveStyledText(
        spec = AdaptiveStyledTextSpec(
            segments = listOf(
                AdaptiveStyledTextDefaults.segment("Regular "),
                AdaptiveStyledTextDefaults.boldSegment("Bold ", color = Color.Blue),
                AdaptiveStyledTextDefaults.italicSegment("Italic ", color = Color.Gray),
                AdaptiveStyledTextDefaults.underlinedSegment("Underlined", color = Color.Red),
            ),
            maxLines = 1,
        ),
    )
}

