package com.example.sparfuchsapp.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val RebateIcon: ImageVector
    get() {
        if (_rebateIcon != null) {
            return _rebateIcon!!
        }
        _rebateIcon = ImageVector.Builder(
            name = "PercentBadge",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8.99044f, 14.9934f)
                lineTo(14.9903f, 8.99282f)
                moveTo(20.9899f, 11.994f)
                curveTo(20.9899f, 13.2624f, 20.3603f, 14.3838f, 19.3966f, 15.0625f)
                curveTo(19.598f, 16.2238f, 19.2503f, 17.4618f, 18.3537f, 18.3586f)
                curveTo(17.457f, 19.2554f, 16.2192f, 19.603f, 15.058f, 19.4016f)
                curveTo(14.3793f, 20.3653f, 13.2582f, 20.9949f, 11.9901f, 20.9949f)
                curveTo(10.7219f, 20.9949f, 9.6008f, 20.3654f, 8.9222f, 19.4017f)
                curveTo(7.7608f, 19.6034f, 6.5227f, 19.2557f, 5.6259f, 18.3588f)
                curveTo(4.7291f, 17.4618f, 4.3815f, 16.2236f, 4.5831f, 15.0622f)
                curveTo(3.6196f, 14.3834f, 2.9902f, 13.2622f, 2.9902f, 11.994f)
                curveTo(2.9902f, 10.7258f, 3.6197f, 9.6046f, 4.5832f, 8.9258f)
                curveTo(4.3817f, 7.7644f, 4.7293f, 6.5263f, 5.6261f, 5.6295f)
                curveTo(6.5228f, 4.7326f, 7.7608f, 4.385f, 8.9221f, 4.5865f)
                curveTo(9.6007f, 3.6228f, 10.7219f, 2.9932f, 11.9901f, 2.9932f)
                curveTo(13.2582f, 2.9932f, 14.3793f, 3.6227f, 15.058f, 4.5864f)
                curveTo(16.2193f, 4.3847f, 17.4574f, 4.7324f, 18.3542f, 5.6293f)
                curveTo(19.251f, 6.5262f, 19.5987f, 7.7644f, 19.3971f, 8.9259f)
                curveTo(20.3605f, 9.6047f, 20.9899f, 10.7258f, 20.9899f, 11.994f)
                close()
                moveTo(9.74042f, 9.74289f)
                horizontalLineTo(9.74792f)
                verticalLineTo(9.75039f)
                horizontalLineTo(9.74042f)
                verticalLineTo(9.74289f)
                close()
                moveTo(10.1154f, 9.74289f)
                curveTo(10.1154f, 9.95f, 9.9475f, 10.1179f, 9.7404f, 10.1179f)
                curveTo(9.5333f, 10.1179f, 9.3654f, 9.95f, 9.3654f, 9.7429f)
                curveTo(9.3654f, 9.5358f, 9.5333f, 9.3679f, 9.7404f, 9.3679f)
                curveTo(9.9475f, 9.3679f, 10.1154f, 9.5358f, 10.1154f, 9.7429f)
                close()
                moveTo(14.2403f, 14.2433f)
                horizontalLineTo(14.2478f)
                verticalLineTo(14.2508f)
                horizontalLineTo(14.2403f)
                verticalLineTo(14.2433f)
                close()
                moveTo(14.6153f, 14.2433f)
                curveTo(14.6153f, 14.4504f, 14.4474f, 14.6184f, 14.2403f, 14.6184f)
                curveTo(14.0332f, 14.6184f, 13.8653f, 14.4504f, 13.8653f, 14.2433f)
                curveTo(13.8653f, 14.0362f, 14.0332f, 13.8683f, 14.2403f, 13.8683f)
                curveTo(14.4474f, 13.8683f, 14.6153f, 14.0362f, 14.6153f, 14.2433f)
                close()
            }
        }.build()
        return _rebateIcon!!
    }

private var _rebateIcon: ImageVector? = null
