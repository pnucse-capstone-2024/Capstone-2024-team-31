package com.wasin.data.data_api

import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.model.handoff.FindAllHandOffResponse
import com.wasin.data.model.handoff.FindBestHandOffRequest
import com.wasin.data.model.handoff.FindBestHandOffResponse
import com.wasin.data.util.ApiUtils

interface HandOffRepository {
    suspend fun findAll(request: FindAllHandOffRequest): ApiUtils.ApiResult<FindAllHandOffResponse>
    suspend fun findBestRouter(request: FindBestHandOffRequest): ApiUtils.ApiResult<FindBestHandOffResponse>
    suspend fun changeHandOffModeAuto(): ApiUtils.ApiResult<String>
    suspend fun changeHandOffModeManual(): ApiUtils.ApiResult<String>
}
