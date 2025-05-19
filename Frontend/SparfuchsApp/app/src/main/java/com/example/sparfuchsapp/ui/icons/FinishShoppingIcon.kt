package com.example.sparfuchsapp.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val FinishShoppingIcon: ImageVector
    get() {
        if (_finishShoppingIcon != null) {
            return _finishShoppingIcon!!
        }
        _finishShoppingIcon = ImageVector.Builder(
            name = "Shopping_cart_checkout",
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
                moveTo(480f, 400f)
                lineToRelative(-56f, -56f)
                lineToRelative(63f, -64f)
                horizontalLineTo(320f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(167f)
                lineToRelative(-64f, -64f)
                lineToRelative(57f, -56f)
                lineToRelative(160f, 160f)
                close()
                moveTo(280f, 880f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(200f, 800f)
                reflectiveQuadToRelative(23.5f, -56.5f)
                reflectiveQuadTo(280f, 720f)
                reflectiveQuadToRelative(56.5f, 23.5f)
                reflectiveQuadTo(360f, 800f)
                reflectiveQuadToRelative(-23.5f, 56.5f)
                reflectiveQuadTo(280f, 880f)
                moveToRelative(400f, 0f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(600f, 800f)
                reflectiveQuadToRelative(23.5f, -56.5f)
                reflectiveQuadTo(680f, 720f)
                reflectiveQuadToRelative(56.5f, 23.5f)
                reflectiveQuadTo(760f, 800f)
                reflectiveQuadToRelative(-23.5f, 56.5f)
                reflectiveQuadTo(680f, 880f)
                moveTo(40f, 160f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(131f)
                lineToRelative(170f, 360f)
                horizontalLineToRelative(280f)
                lineToRelative(156f, -280f)
                horizontalLineToRelative(91f)
                lineTo(692f, 478f)
                quadToRelative(-11f, 20f, -29.5f, 31f)
                reflectiveQuadTo(622f, 520f)
                horizontalLineTo(324f)
                lineToRelative(-44f, 80f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(80f)
                horizontalLineTo(280f)
                quadToRelative(-45f, 0f, -68.5f, -39f)
                reflectiveQuadToRelative(-1.5f, -79f)
                lineToRelative(54f, -98f)
                lineToRelative(-144f, -304f)
                close()
            }
        }.build()
        return _finishShoppingIcon!!
    }

private var _finishShoppingIcon: ImageVector? = null
