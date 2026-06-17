package com.gopal.flexible_icon

import android.graphics.BitmapFactory
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

/**
 * Displays an icon from any supported source type.
 *
 * Supports vector drawables, drawable resources, bitmap resources, and raw [ImageBitmap] data.
 * The source type is determined by [FlexibleIconSource].
 *
 * @param source The icon source — determines which type of asset is rendered.
 * @param modifier Modifier to be applied to the icon.
 * @param tint Tint color applied to the icon. Defaults to [Color.Unspecified] (no tint).
 *
 * @see FlexibleIconSource
 */
@Composable
fun FlexibleIcon(
    source: FlexibleIconSource,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    when (source) {
        is FlexibleIconSource.VectorIcon -> {
            Icon(
                imageVector = source.imageVector,
                contentDescription = stringResource(source.contentDescriptionResource),
                modifier = modifier,
                tint = tint
            )
        }
        is FlexibleIconSource.DrawableResourceIcon -> {
            Icon(
                painter = painterResource(source.drawableRes),
                contentDescription = stringResource(source.contentDescriptionResource),
                modifier = modifier,
                tint = tint
            )
        }
        is FlexibleIconSource.BitmapResourceIcon -> {
            Icon(
                bitmap = loadImageBitmapFromRes(source.bitmapRes),
                contentDescription = stringResource(source.contentDescriptionResource),
                modifier = modifier,
                tint = tint
            )
        }
        is FlexibleIconSource.BitmapIcon -> {
            Icon(
                bitmap = source.bitmapData,
                contentDescription = stringResource(source.contentDescriptionResource),
                modifier = modifier,
                tint = tint
            )
        }
    }
}

@Composable
private fun loadImageBitmapFromRes(resId: Int): ImageBitmap {
    val resources = LocalContext.current.resources
    return remember(resId) {
        BitmapFactory.decodeResource(resources, resId).asImageBitmap()
    }
}

@Preview(showBackground = true)
@Composable
private fun FlexibleIconPreview() {
    FlexibleIcon(
        source = FlexibleIconSource.DrawableResourceIcon(
            drawableRes = android.R.drawable.ic_dialog_info,
            contentDescriptionResource = android.R.string.ok
        )
    )
}

