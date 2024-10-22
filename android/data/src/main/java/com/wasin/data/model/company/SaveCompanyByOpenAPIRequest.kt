package com.wasin.data.model.company

import kotlinx.serialization.Serializable

@Serializable
data class SaveCompanyByOpenAPIRequest (
    val serviceKey: String = "",
    val companyFssId: String = "",
    val location: String = "",
    val companyName: String = ""
)
