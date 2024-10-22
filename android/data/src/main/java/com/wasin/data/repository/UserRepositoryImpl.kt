package com.wasin.data.repository

import com.wasin.data.api.UserApi
import com.wasin.data.data_api.UserRepository
import com.wasin.data.model.user.EmailCheckRequest
import com.wasin.data.model.user.EmailRequest
import com.wasin.data.model.user.LockConfirmRequest
import com.wasin.data.model.user.LockRequest
import com.wasin.data.model.user.LoginRequest
import com.wasin.data.model.user.LoginResponse
import com.wasin.data.model.user.SignupRequest
import com.wasin.data.util.ApiUtils
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
    override suspend fun signup(signupRequest: SignupRequest): ApiUtils.ApiResult<String> {
        return userApi.signup(signupRequest)
    }

    override suspend fun login(loginRequest: LoginRequest): ApiUtils.ApiResult<LoginResponse> {
        return userApi.login(loginRequest)
    }

    override suspend fun logout(): ApiUtils.ApiResult<String> {
        return userApi.logout()
    }

    override suspend fun withdraw(): ApiUtils.ApiResult<String> {
        return userApi.withdraw()
    }

    override suspend fun lock(lockRequest: LockRequest): ApiUtils.ApiResult<String> {
        return userApi.lock(lockRequest)
    }

    override suspend fun lockConfirm(lockConfirmRequest: LockConfirmRequest): ApiUtils.ApiResult<String> {
        return userApi.lockConfirm(lockConfirmRequest)
    }

    override suspend fun emailSend(emailRequest: EmailRequest): ApiUtils.ApiResult<String> {
        return userApi.emailSend(emailRequest)
    }

    override suspend fun emailCheck(emailCheckRequest: EmailCheckRequest): ApiUtils.ApiResult<String> {
        return userApi.emailCheck(emailCheckRequest)
    }

}
