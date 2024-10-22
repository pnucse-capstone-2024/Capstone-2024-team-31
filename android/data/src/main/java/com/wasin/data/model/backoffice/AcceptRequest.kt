package com.wasin.data.model.backoffice

import kotlinx.serialization.Serializable

@Serializable
data class AcceptRequest(
    val userId: Long
)
