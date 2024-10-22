package com.wasin.data.data_api

import com.wasin.data.model.router.CheckRouterResponse
import com.wasin.data.model.router.CreateRouterRequest
import com.wasin.data.model.router.FindAllRouterResponse
import com.wasin.data.model.router.FindByRouterIdResponse
import com.wasin.data.model.router.FindCompanyImageResponse
import com.wasin.data.model.router.LogEmailRequest
import com.wasin.data.model.router.LogRouterResponse
import com.wasin.data.model.router.UpdateRouterRequest
import com.wasin.data.util.ApiUtils

interface RouterRepository {
    suspend fun findAll(): ApiUtils.ApiResult<FindAllRouterResponse>
    suspend fun findByRouterId(routerId: Long): ApiUtils.ApiResult<FindByRouterIdResponse>
    suspend fun create(request: CreateRouterRequest): ApiUtils.ApiResult<String>
    suspend fun update(request: UpdateRouterRequest, routerId: Long): ApiUtils.ApiResult<String>
    suspend fun delete(routerId: Long): ApiUtils.ApiResult<String>
    suspend fun findCompanyImage(): ApiUtils.ApiResult<FindCompanyImageResponse>
    suspend fun checkRouter(routerId: Long): ApiUtils.ApiResult<CheckRouterResponse>
    suspend fun logRouter(routerId: Long): ApiUtils.ApiResult<LogRouterResponse>
    suspend fun logEmail(routerId: Long, request: LogEmailRequest): ApiUtils.ApiResult<String>
}
