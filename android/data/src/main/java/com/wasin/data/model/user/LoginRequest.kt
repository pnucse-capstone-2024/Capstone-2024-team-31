package com.wasin.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val email: String = "",
    val password: String = "",
    val fcmToken: String = ""
)
