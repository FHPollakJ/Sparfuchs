package com.example.sparfuchsapp.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BarcodeScannerIcon: ImageVector
    get() {
        if (_barcodeScannerIcon != null) {
            return _barcodeScannerIcon!!
        }
        _barcodeScannerIcon = ImageVector.Builder(
            name = "Barcode_scanner",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(40f, 840f)
                verticalLineToRelative(-200f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(680f, 0f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(200f)
                close()
                moveTo(160f, 720f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(480f)
                close()
                moveToRelative(120f, 0f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(40f)
                verticalLineToRelative(480f)
                close()
                moveToRelative(120f, 0f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(480f)
                close()
                moveToRelative(120f, 0f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(480f)
                close()
                moveToRelative(160f, 0f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(40f)
                verticalLineToRelative(480f)
                close()
                moveToRelative(80f, 0f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(40f)
                verticalLineToRelative(480f)
                close()
                moveTo(40f, 320f)
                verticalLineToRelative(-200f)
                horizontalLineToRelative(200f)
                verticalLineToRelative(80f)
                horizontalLineTo(120f)
                verticalLineToRelative(120f)
                close()
                moveToRelative(800f, 0f)
                verticalLineToRelative(-120f)
                horizontalLineTo(720f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(200f)
                verticalLineToRelative(200f)
                close()
            }
        }.build()
        return _barcodeScannerIcon!!
    }

private var _barcodeScannerIcon: ImageVector? = null