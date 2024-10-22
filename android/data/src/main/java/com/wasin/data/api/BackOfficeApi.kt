package com.wasin.data.api

import com.wasin.data._const.HttpRoutes
import com.wasin.data.data_api.BackOfficeRepository
import com.wasin.data.model.backoffice.AcceptRequest
import com.wasin.data.model.backoffice.FindWaitingListResponse
import com.wasin.data.util.ApiUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class BackOfficeApi(
    private val client: HttpClient
): BackOfficeRepository {
    override suspend fun findWaitingList(): ApiUtils.ApiResult<FindWaitingListResponse> {
        return client.get(HttpRoutes.GET_WAITING_LIST.getPath1()).body()
    }

    override suspend fun accept(request: AcceptRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.BACKOFFICE_ACCEPT.getPath1()) {
            setBody(request)
        }.body()
    }

}
