package com.eviarch.clock.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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