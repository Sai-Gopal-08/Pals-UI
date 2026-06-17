package com.gopal.flexible_icon

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the source of an icon for [FlexibleIcon].
 *
 * Each subtype represents a different asset format. All subtypes are stable `data class`es,
 * and the interface is marked `@Stable` so the Compose compiler can verify stability
 * when [FlexibleIconSource] is used as a composable parameter — preventing unnecessary recompositions.
 */
@Stable
sealed interface FlexibleIconSource {
    /**
     * The string resource ID used as the icon's accessibility content description.
     */
    @get:StringRes
    val contentDescriptionResource: Int

    /**
     * An icon backed by an [ImageVector] — suitable for Material icons or XML vector drawables
     * loaded as vectors.
     *
     * @property imageVector The vector graphic to render.
     * @property contentDescriptionResource String resource ID for accessibility.
     */
    data class VectorIcon(
        val imageVector: ImageVector,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource

    /**
     * An icon backed by a drawable resource ID — suitable for PNG, XML, or any
     * `@DrawableRes` asset rendered via [androidx.compose.ui.res.painterResource].
     *
     * @property drawableRes The drawable resource ID to load.
     * @property contentDescriptionResource String resource ID for accessibility.
     */
    data class DrawableResourceIcon(
        @get:DrawableRes val drawableRes: Int,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource

    /**
     * An icon backed by a bitmap drawable resource — decoded at runtime using
     * [android.graphics.BitmapFactory] and cached via `remember`.
     *
     * @property bitmapRes The drawable resource ID of the bitmap to decode.
     * @property contentDescriptionResource String resource ID for accessibility.
     */
    data class BitmapResourceIcon(
        @get:DrawableRes val bitmapRes: Int,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource

    /**
     * An icon backed by an already-decoded [ImageBitmap] — suitable for dynamically
     * loaded or programmatically generated bitmaps.
     *
     * @property bitmapData The decoded bitmap to render.
     * @property contentDescriptionResource String resource ID for accessibility.
     */
    data class BitmapIcon(
        val bitmapData: ImageBitmap,
        override val contentDescriptionResource: Int
    ) : FlexibleIconSource
}

