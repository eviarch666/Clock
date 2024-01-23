package com.eviarch.clock.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WaveCircle(
    modifier: Modifier = Modifier,
    edgeNumber: Int = 12,
    radian: Float,
    rotationAngle: Float = 0f,
    color: Color,
    strokeWidth: Float = 20f,
) {
    Canvas(
        modifier = modifier,
        onDraw = {
            val centerX = size.width / 2
            val centerY = size.height / 2

            // 计算半径，以确保图形等比例放大
            val radius = (size.width / 2) - (strokeWidth / 2)

            val numberOfPoints = 360
            val angleIncrement = 360f / numberOfPoints

            val path = Path()
            for (i in 0 until numberOfPoints) {
                val angleInRadians = Math.toRadians(i.toDouble() * angleIncrement)

                val x = (centerX + (radius + sin(angleInRadians * edgeNumber) * radian) * cos(
                    angleInRadians
                )).toFloat()
                val y = (centerY + (radius + sin(angleInRadians * edgeNumber) * radian) * sin(
                    angleInRadians
                )).toFloat()

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()

            // Default rotation
            val defaultRotationAngle = -8f
            rotate(defaultRotationAngle + rotationAngle, pivot = Offset(centerX, centerY)) {
                drawPath(
                    path = path,
                    color = color,
                    style = Fill
                )
            }
        }
    )
}