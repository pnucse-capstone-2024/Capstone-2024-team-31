package com.wasin.data.api

import com.wasin.data._const.HttpRoutes
import com.wasin.data.data_api.ProfileRepository
import com.wasin.data.model.profile.FindAllProfileResponse
import com.wasin.data.util.ApiUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class ProfileApi(
    private val client: HttpClient
): ProfileRepository {
    override suspend fun findAllProfile(): ApiUtils.ApiResult<FindAllProfileResponse> {
        return client.get(HttpRoutes.GET_PROFILE.getPath1()).body()
    }

    override suspend fun changeModeAuto(): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.PROFILE_MODE_AUTO_CHANGE.getPath1()).body()
    }

    override suspend fun changeModeManual(): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.PROFILE_MODE_MANUAL_CHANGE.getPath1()).body()
    }

    override suspend fun changeProfile(profileId: Long): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.UPDATE_PROFILE.getPath1() + "/${profileId}").body()
    }
}
