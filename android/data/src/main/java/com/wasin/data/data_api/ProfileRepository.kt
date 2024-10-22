package com.wasin.data.data_api

import com.wasin.data.model.profile.FindAllProfileResponse
import com.wasin.data.util.ApiUtils

interface ProfileRepository {
    suspend fun findAllProfile(): ApiUtils.ApiResult<FindAllProfileResponse>
    suspend fun changeModeAuto(): ApiUtils.ApiResult<String>
    suspend fun changeModeManual(): ApiUtils.ApiResult<String>
    suspend fun changeProfile(profileId: Long): ApiUtils.ApiResult<String>
}
