package com.wasin.presentation._util

sealed class WasinEvent {
    data object Loading: WasinEvent()
    data object Navigate: WasinEvent()
    data class MakeToast(val message: String): WasinEvent()
}
