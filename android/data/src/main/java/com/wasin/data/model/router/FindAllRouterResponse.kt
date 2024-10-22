package com.wasin.data.model.router

import kotlinx.serialization.Serializable

@Serializable
data class FindAllRouterResponse(
    val image: CompanyImageDTO = CompanyImageDTO(),
    val routerList: List<EachRouter> = emptyList()
) {
    @Serializable
    data class CompanyImageDTO(
         val companyImage: String = "",
         val imageHeight: Int = 0,
         val imageWidth: Int = 0
    )

    @Serializable
    data class EachRouter(
        val routerId: Long = 0,
        val name: String = "",
        val score: Long = 0L,
        val positionX: Double = 0.0,
        val positionY: Double = 0.0
    )
}
