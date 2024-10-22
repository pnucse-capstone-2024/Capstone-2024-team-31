package com.wasin.data.model.company

import kotlinx.serialization.Serializable

@Serializable
data class FindAllCompanyByDBResponse(
    val companyDBList: List<CompanyDBItem>
) {
    @Serializable
    data class CompanyDBItem (
        val companyId: Long,
        val location: String,
        val companyName: String
    )
}
