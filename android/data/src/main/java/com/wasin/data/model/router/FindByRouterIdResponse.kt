package com.wasin.data.model.router

import kotlinx.serialization.Serializable

@Serializable
data class FindByRouterIdResponse(
    val image: CompanyImageDTO = CompanyImageDTO(),
    val information: RouterInformation = RouterInformation()
) {
    @Serializable
    data class CompanyImageDTO(
        val companyImage: String = "",
        val imageHeight: Int = 0,
        val imageWidth: Int = 0
    )

    @Serializable
    data class RouterInformation(
        val name: String = "",
        val ssid: String = "",
        val macAddress: String = "",
        val instance: String = "",
        val serialNumber: String = "",
        val password: String = "",
        val port: String = "",
        val score: Long = 0,
        val positionX: Double = 0.0,
        val positionY: Double = 0.0,
    )
}
