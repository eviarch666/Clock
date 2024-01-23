package com.eviarch.clock.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.sin
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Preview(name = "WholeTheme")
@Composable
fun WholeTheme (){
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SystemBar(
            statusBarColor = MaterialTheme.colorScheme.background,
            navigationBarColor = MaterialTheme.colorScheme.inverseOnSurface
        )
        TimeNumber()
    }
}
@Composable
fun SystemBar(
    statusBarColor: Color,
    navigationBarColor: Color
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.apply {
            setStatusBarColor(color = statusBarColor)
            setNavigationBarColor(color = navigationBarColor)
        }
    }
}
@Composable
fun TimeNumber (){
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var boxWidth by remember { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }

    LaunchedEffect(true) {
        while (true) {
            currentTime = LocalTime.now()
            delay(1000) // 每秒更新一次
        }
    }
    Column {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .weight(5f)
                .onGloballyPositioned { coordinates ->
                    // 获取 Box 的全局宽度和高度
                    boxWidth = coordinates.size.width.toFloat()
                    boxHeight = coordinates.size.height.toFloat()
                }
        ){
            WaveCircle(
                edgeNumber = 12,
                radian = 20f,
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .width(256.dp)
                    .align(Alignment.Center),
                strokeWidth = 0f
            )
            Clock(radius = minOf(boxWidth,boxHeight) / 4,currentTime = currentTime)
        }
        Row (
            modifier = Modifier
                .weight(1f)
        ){
            Spacer(
                modifier = Modifier
                    .weight(2f)
            )
            Button(//Hour
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    )
                    .weight(3f),
                onClick = {}
            ){
                Text(
                    modifier = Modifier
                    ,
                    text = currentTime.hour.toString()
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Button(//Minute
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    )
                    .weight(3f),
                onClick = {}
            ){
                Text(
                    modifier = Modifier
                    ,
                    text = currentTime.minute.toString()
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Button(//Second
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    )
                    .weight(3f),
                onClick = {}
            ){
                Text(
                    modifier = Modifier
                    ,
                    text = currentTime.second.toString()
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun Clock (radius: Float, currentTime: LocalTime){
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ){
        val centerX = size.width / 2
        val centerY = size.height / 2

        drawClockHandle(centerX = centerX, centerY = centerY, radius = radius, currentTime = currentTime, this)
    }
}
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

private fun DrawScope.drawClockHandle (centerX: Float, centerY: Float, radius: Float, currentTime: LocalTime, drawScope: DrawScope ){
    val cornerRadius = 8.dp.toPx()//圆角矩形的圆角半径

    drawScope.rotate(currentTime.hour * 30f - 180f, Offset(centerX, centerY)) {
        drawRoundRect(
            color = Color.White,
            size = Size(12.dp.toPx(), radius * 0.6f),
            topLeft = Offset(centerX - 8.dp.toPx(), centerY),
            style = Stroke(width = 8.dp.toPx()),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }

    // 分钟指针
    drawScope.rotate(currentTime.minute * 6f- 180f, Offset(centerX, centerY)) {
        drawRoundRect(
            color = Color.White,
            size = Size(8.dp.toPx(), radius * 0.8f),
            topLeft = Offset(centerX - 4.dp.toPx(), centerY),
            style = Stroke(width = 4.dp.toPx()),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }

    // 秒针
    drawScope.rotate(currentTime.second * 6f- 180f, Offset(centerX, centerY)) {
        drawRoundRect(
            color = Color.Red,
            size = Size(4.dp.toPx(), radius * 1.1f),
            topLeft = Offset(centerX - 2.dp.toPx(), centerY),
            style = Stroke(width = 2.dp.toPx()),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }
}