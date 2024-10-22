package com.wasin.data.api

import com.wasin.data._const.HttpRoutes
import com.wasin.data.data_api.HandOffRepository
import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.model.handoff.FindAllHandOffResponse
import com.wasin.data.model.handoff.FindBestHandOffRequest
import com.wasin.data.model.handoff.FindBestHandOffResponse
import com.wasin.data.util.ApiUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class HandOffApi (
    private val client: HttpClient
): HandOffRepository {
    override suspend fun findAll(request: FindAllHandOffRequest): ApiUtils.ApiResult<FindAllHandOffResponse> {
        return client.post(HttpRoutes.GET_HAND_OFF_ALL.getPath1()) {
            setBody(request)
        }.body()
    }

    override suspend fun findBestRouter(request: FindBestHandOffRequest): ApiUtils.ApiResult<FindBestHandOffResponse> {
        return client.post(HttpRoutes.GET_HAND_OFF_BEST.getPath1()) {
            setBody(request)
        }.body()
    }

    override suspend fun changeHandOffModeAuto(): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.CHANGE_MODE_AUTO.getPath1()).body()
    }

    override suspend fun changeHandOffModeManual(): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.CHANGE_MODE_MANUAL.getPath1()).body()
    }
}
