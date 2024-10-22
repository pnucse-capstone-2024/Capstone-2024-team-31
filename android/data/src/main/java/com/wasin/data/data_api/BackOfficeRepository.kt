package com.wasin.data.data_api

import com.wasin.data.model.backoffice.AcceptRequest
import com.wasin.data.model.backoffice.FindWaitingListResponse
import com.wasin.data.util.ApiUtils

interface BackOfficeRepository {
    suspend fun findWaitingList(): ApiUtils.ApiResult<FindWaitingListResponse>
    suspend fun accept(request: AcceptRequest): ApiUtils.ApiResult<String>
}
