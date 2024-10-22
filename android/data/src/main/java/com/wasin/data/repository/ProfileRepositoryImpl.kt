package com.wasin.data.repository

import com.wasin.data.api.ProfileApi
import com.wasin.data.data_api.ProfileRepository
import com.wasin.data.model.profile.FindAllProfileResponse
import com.wasin.data.util.ApiUtils
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
): ProfileRepository {
    override suspend fun findAllProfile(): ApiUtils.ApiResult<FindAllProfileResponse> {
        return profileApi.findAllProfile()
    }

    override suspend fun changeModeAuto(): ApiUtils.ApiResult<String> {
        return profileApi.changeModeAuto()
    }

    override suspend fun changeModeManual(): ApiUtils.ApiResult<String> {
        return profileApi.changeModeManual()
    }

    override suspend fun changeProfile(profileId: Long): ApiUtils.ApiResult<String> {
        return profileApi.changeProfile(profileId)
    }

}
