package com.wasin.presentation.router_add

sealed class RouterAddEvent {
    data class EnterName(val name: String): RouterAddEvent()
    data class EnterMacAddress(val macAddress: String): RouterAddEvent()
    data class EnterPosition(val x: Float, val y: Float): RouterAddEvent()
    data class EnterSerialNumber(val serialNumber: String): RouterAddEvent()
    data class EnterPort(val port: String): RouterAddEvent()
    data class EnterPassword(val password: String): RouterAddEvent()
    data class EnterImageSize(val width: Int): RouterAddEvent()
    data object SaveRouter: RouterAddEvent()
}
