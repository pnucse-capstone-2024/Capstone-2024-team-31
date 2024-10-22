package com.wasin.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class EmailCheckRequest(
    val email: String = "",
    val code: String = ""
)
