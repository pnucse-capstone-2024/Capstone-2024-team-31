package com.wasin.data.data_api

import com.wasin.data.model.user.EmailCheckRequest
import com.wasin.data.model.user.EmailRequest
import com.wasin.data.model.user.LockConfirmRequest
import com.wasin.data.model.user.LockRequest
import com.wasin.data.model.user.LoginRequest
import com.wasin.data.model.user.LoginResponse
import com.wasin.data.model.user.SignupRequest
import com.wasin.data.util.ApiUtils

interface UserRepository {
    suspend fun signup(signupRequest: SignupRequest): ApiUtils.ApiResult<String>
    suspend fun login(loginRequest: LoginRequest): ApiUtils.ApiResult<LoginResponse>
    suspend fun logout(): ApiUtils.ApiResult<String>
    suspend fun withdraw(): ApiUtils.ApiResult<String>
    suspend fun lock(lockRequest: LockRequest): ApiUtils.ApiResult<String>
    suspend fun lockConfirm(lockConfirmRequest: LockConfirmRequest): ApiUtils.ApiResult<String>
    suspend fun emailSend(emailRequest: EmailRequest): ApiUtils.ApiResult<String>
    suspend fun emailCheck(emailCheckRequest: EmailCheckRequest): ApiUtils.ApiResult<String>
}
