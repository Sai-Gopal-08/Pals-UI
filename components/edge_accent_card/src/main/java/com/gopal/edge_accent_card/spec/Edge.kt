package com.gopal.edge_accent_card.spec

import androidx.compose.runtime.Stable

/**
 * Defines which card edge to decorate and how corners are handled.
 *
 * Marked `@Stable` so the Compose compiler can verify stability when used as a parameter.
 */
@Stable
sealed class Edge {
    protected val topSweepAngleErrorMessage = "topSweepAngle must be between 0f and 90f"
    protected val bottomSweepAngleErrorMessage = "bottomSweepAngle must be between 0f and 90f"

    sealed class Left : Edge() {

        /**
         * Left edge including corner arcs.
         * The [EdgeSpan] of [EdgeDecoration] is ignored for this type — always drawn full height.
         */
        data class WithCorners(
            val topSweepAngle: Float = 60f,
            val bottomSweepAngle: Float = 60f,
            val arcStrokeWidthMultiplier: Float? = null,
        ) : Left() {
            init {
                require(topSweepAngle in 0f..90f) { topSweepAngleErrorMessage }
                require(bottomSweepAngle in 0f..90f) { bottomSweepAngleErrorMessage }
            }
        }

        /** Left edge without corner arcs. Respects [EdgeDecoration.span]. */
        data object WithoutCorners : Left()
    }

    sealed class Right : Edge() {

        /**
         * Right edge including corner arcs.
         * The [EdgeSpan] of [EdgeDecoration] is ignored for this type — always drawn full height.
         */
        data class WithCorners(
            val topSweepAngle: Float = 60f,
            val bottomSweepAngle: Float = 60f,
            val arcStrokeWidthMultiplier: Float? = null,
        ) : Right() {
            init {
                require(topSweepAngle in 0f..90f) { topSweepAngleErrorMessage }
                require(bottomSweepAngle in 0f..90f) { bottomSweepAngleErrorMessage }
            }
        }

        /** Right edge without corner arcs. Respects [EdgeDecoration.span]. */
        data object WithoutCorners : Right()
    }

    /** Top edge. Respects [EdgeDecoration.span] (horizontal fraction). */
    data object Top : Edge()

    /** Bottom edge. Respects [EdgeDecoration.span] (horizontal fraction). */
    data object Bottom : Edge()
}

