package com.wasin.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String = ""
)
