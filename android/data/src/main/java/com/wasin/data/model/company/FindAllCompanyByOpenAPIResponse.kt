package com.wasin.data.model.company

import kotlinx.serialization.Serializable

@Serializable
data class FindAllCompanyByOpenAPIResponse (
    val companyOpenAPIList: List<CompanyOpenAPIItem>
) {
    @Serializable
    data class CompanyOpenAPIItem (
        val companyFssId: String,
        val location: String,
        val companyName: String
    )
}
