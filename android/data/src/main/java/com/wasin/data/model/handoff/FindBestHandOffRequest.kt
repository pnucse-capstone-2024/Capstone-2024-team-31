package com.wasin.data.model.handoff

import kotlinx.serialization.Serializable

@Serializable
data class FindBestHandOffRequest (
    val router: List<RouterDTO> = emptyList()
) {
    @Serializable
    data class RouterDTO(
        val ssid: String,
        val macAddress: String,
        val level: Long,
        val detailLevel: Long
    )
}
