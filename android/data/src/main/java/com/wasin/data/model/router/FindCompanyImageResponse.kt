package com.wasin.data.model.router

import kotlinx.serialization.Serializable

@Serializable
data class FindCompanyImageResponse (
    val companyImage: String = "",
    val imageHeight: Int = 0,
    val imageWidth: Int = 0
)
