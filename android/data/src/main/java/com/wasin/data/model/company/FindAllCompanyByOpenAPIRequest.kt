package com.wasin.data.model.company

import kotlinx.serialization.Serializable

@Serializable
data class FindAllCompanyByOpenAPIRequest (
    val companyName: String
)
