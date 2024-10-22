package com.wasin.data.data_api

import com.wasin.data.model.company.FindAllCompanyByDBResponse
import com.wasin.data.model.company.FindAllCompanyByOpenAPIRequest
import com.wasin.data.model.company.FindAllCompanyByOpenAPIResponse
import com.wasin.data.model.company.SaveCompanyByDBRequest
import com.wasin.data.model.company.SaveCompanyByOpenAPIRequest
import com.wasin.data.util.ApiUtils
import java.io.File

interface CompanyRepository {
    suspend fun findAllCompanyByOpenAPI(request: FindAllCompanyByOpenAPIRequest): ApiUtils.ApiResult<FindAllCompanyByOpenAPIResponse>
    suspend fun findAllCompanyByDB(): ApiUtils.ApiResult<FindAllCompanyByDBResponse>
    suspend fun saveCompanyByOpenAPI(data: SaveCompanyByOpenAPIRequest, file: File): ApiUtils.ApiResult<String>
    suspend fun saveCompanyByDB(data: SaveCompanyByDBRequest): ApiUtils.ApiResult<String>
}
