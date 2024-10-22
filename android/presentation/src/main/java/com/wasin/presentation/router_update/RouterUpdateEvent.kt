package com.wasin.presentation.router_update

sealed class RouterUpdateEvent {
    data class EnterName(val name: String): RouterUpdateEvent()
    data class EnterPosition(val x: Float, val y: Float): RouterUpdateEvent()
    data class EnterPassword(val password: String): RouterUpdateEvent()
    data class EnterImageSize(val width: Int): RouterUpdateEvent()
    data object UpdateRouter: RouterUpdateEvent()
}
