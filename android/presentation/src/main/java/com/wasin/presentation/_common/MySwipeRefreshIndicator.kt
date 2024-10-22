package com.wasin.presentation._common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.wasin.presentation._theme.main_blue

@Composable
fun MySwipeRefreshIndicator(
    state: SwipeRefreshState,
    refreshTrigger: Dp
) {
    SwipeRefreshIndicator(
        state = state,
        refreshTriggerDistance = refreshTrigger,
        backgroundColor = Color.White,
        contentColor = main_blue
    )
}
