package com.wasin.data.repository

import com.wasin.data.api.BackOfficeApi
import com.wasin.data.data_api.BackOfficeRepository
import com.wasin.data.model.backoffice.AcceptRequest
import com.wasin.data.model.backoffice.FindWaitingListResponse
import com.wasin.data.util.ApiUtils
import javax.inject.Inject

class BackOfficeRepositoryImpl @Inject constructor(
    private val backOfficeApi: BackOfficeApi
): BackOfficeRepository {
    override suspend fun findWaitingList(): ApiUtils.ApiResult<FindWaitingListResponse> {
        return backOfficeApi.findWaitingList()
    }

    override suspend fun accept(request: AcceptRequest): ApiUtils.ApiResult<String> {
        return backOfficeApi.accept(request)
    }
}
