package com.wasin.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class ReissueResponse(
    val accessToken: String,
    val refreshToken: String
)
