package com.wasin.data.model.handoff

import kotlinx.serialization.Serializable

@Serializable
data class FindBestHandOffResponse(
    val isAuto: Boolean = false,
    val router: RouterWithStateDTO = RouterWithStateDTO()
) {
    @Serializable
    data class RouterWithStateDTO(
        val level: Long = 0,
        val detailLevel: Long = 0,
        val score: Long = 0,
        val ssid: String = "",
        val macAddress: String = "",
        val password: String ="",
        val isSystemExist: Boolean = true
    )
}
