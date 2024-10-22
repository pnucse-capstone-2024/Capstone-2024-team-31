package com.wasin.data.repository

import com.wasin.data.api.HandOffApi
import com.wasin.data.data_api.HandOffRepository
import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.model.handoff.FindAllHandOffResponse
import com.wasin.data.model.handoff.FindBestHandOffRequest
import com.wasin.data.model.handoff.FindBestHandOffResponse
import com.wasin.data.util.ApiUtils
import javax.inject.Inject

class HandOffRepositoryImpl @Inject constructor(
    private val handOffApi: HandOffApi
): HandOffRepository {
    override suspend fun findAll(request: FindAllHandOffRequest): ApiUtils.ApiResult<FindAllHandOffResponse> {
        return handOffApi.findAll(request)
    }

    override suspend fun findBestRouter(request: FindBestHandOffRequest): ApiUtils.ApiResult<FindBestHandOffResponse> {
        return handOffApi.findBestRouter(request)
    }

    override suspend fun changeHandOffModeAuto(): ApiUtils.ApiResult<String> {
        return handOffApi.changeHandOffModeAuto()
    }

    override suspend fun changeHandOffModeManual(): ApiUtils.ApiResult<String> {
        return handOffApi.changeHandOffModeManual()
    }
}
