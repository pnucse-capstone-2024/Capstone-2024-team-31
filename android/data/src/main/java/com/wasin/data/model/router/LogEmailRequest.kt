package com.wasin.data.model.router

import kotlinx.serialization.Serializable

@Serializable
data class LogEmailRequest(
    val log: String = ""
)
