package com.wasin.data.repository

import com.wasin.data.api.CompanyApi
import com.wasin.data.data_api.CompanyRepository
import com.wasin.data.model.company.FindAllCompanyByDBResponse
import com.wasin.data.model.company.FindAllCompanyByOpenAPIRequest
import com.wasin.data.model.company.FindAllCompanyByOpenAPIResponse
import com.wasin.data.model.company.SaveCompanyByDBRequest
import com.wasin.data.model.company.SaveCompanyByOpenAPIRequest
import com.wasin.data.util.ApiUtils
import java.io.File
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyApi: CompanyApi
): CompanyRepository {
    override suspend fun findAllCompanyByOpenAPI(request: FindAllCompanyByOpenAPIRequest): ApiUtils.ApiResult<FindAllCompanyByOpenAPIResponse> {
        return companyApi.findAllCompanyByOpenAPI(request)
    }

    override suspend fun findAllCompanyByDB(): ApiUtils.ApiResult<FindAllCompanyByDBResponse> {
        return companyApi.findAllCompanyByDB()
    }

    override suspend fun saveCompanyByOpenAPI(
        data: SaveCompanyByOpenAPIRequest,
        file: File
    ): ApiUtils.ApiResult<String> {
        return companyApi.saveCompanyByOpenAPI(data, file)
    }

    override suspend fun saveCompanyByDB(data: SaveCompanyByDBRequest): ApiUtils.ApiResult<String> {
        return companyApi.saveCompanyByDB(data)
    }
}
