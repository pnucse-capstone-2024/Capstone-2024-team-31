package com.wasin.data.model.company

import kotlinx.serialization.Serializable

@Serializable
data class SaveCompanyByDBRequest (
    val companyId: Long
)
