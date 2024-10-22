package com.wasin.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val role: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val password2: String = "",
)
