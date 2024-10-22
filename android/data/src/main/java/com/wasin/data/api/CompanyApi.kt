package com.wasin.data.api

import com.wasin.data._const.HttpRoutes
import com.wasin.data.data_api.CompanyRepository
import com.wasin.data.model.company.FindAllCompanyByDBResponse
import com.wasin.data.model.company.FindAllCompanyByOpenAPIRequest
import com.wasin.data.model.company.FindAllCompanyByOpenAPIResponse
import com.wasin.data.model.company.SaveCompanyByDBRequest
import com.wasin.data.model.company.SaveCompanyByOpenAPIRequest
import com.wasin.data.util.ApiUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class CompanyApi (
    private val client: HttpClient
): CompanyRepository {

    override suspend fun findAllCompanyByOpenAPI(request: FindAllCompanyByOpenAPIRequest): ApiUtils.ApiResult<FindAllCompanyByOpenAPIResponse> {
        return client.post(HttpRoutes.GET_OPEN_API.getPath1()) {
            setBody(request)
        }.body()
    }

    override suspend fun findAllCompanyByDB(): ApiUtils.ApiResult<FindAllCompanyByDBResponse> {
        return client.get(HttpRoutes.GET_DB.getPath1()).body()
    }

    override suspend fun saveCompanyByOpenAPI(
        data: SaveCompanyByOpenAPIRequest,
        file: File
    ): ApiUtils.ApiResult<String> {
        return client.submitFormWithBinaryData(
            url = HttpRoutes.SAVE_COMPANY_OPEN_API.getPath1(),
            formData = formData {
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                })
                append("data", Json.encodeToString(data), Headers.build {
                    append(HttpHeaders.ContentType, "application/json")
                })
            }
        ).body()
    }

    override suspend fun saveCompanyByDB(data: SaveCompanyByDBRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.SAVE_COMPANY_DB.getPath1()){
            setBody(data)
        }.body()
    }

}
