package com.wasin.presentation._util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LaunchedEffectEvent(
    eventFlow: SharedFlow<WasinEvent>,
    onNavigate: () -> Unit = {},
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is WasinEvent.Navigate -> onNavigate()
                is WasinEvent.MakeToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is WasinEvent.Loading -> { }
            }
        }
    }
}
