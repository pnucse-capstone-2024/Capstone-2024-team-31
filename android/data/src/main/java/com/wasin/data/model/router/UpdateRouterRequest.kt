package com.wasin.data.model.router

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRouterRequest(
    val name: String = "",
    val password: String = "",
    val positionX: Double = 0.0,
    val positionY: Double = 0.0
)
