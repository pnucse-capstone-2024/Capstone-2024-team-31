package com.wasin.presentation._theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import com.wasin.presentation._common.addFocusCleaner

@Composable
fun WasinAppTheme(
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
){
    val focusManager = LocalFocusManager.current
    WasinTheme {
        Scaffold(
            bottomBar = bottomBar,
        ) {
            Surface(
                modifier = Modifier
                    .background(Color.White)
                    .addFocusCleaner(focusManager)
                    .padding(Dimens.ScreenPadding)
                    .fillMaxSize()
                    .padding(it),
                color = Color.White,
                content = content
            )
        }
    }
}
