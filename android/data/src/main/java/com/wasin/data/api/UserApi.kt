package com.wasin.data.api

import com.wasin.data._const.HttpRoutes
import com.wasin.data.data_api.UserRepository
import com.wasin.data.model.user.EmailCheckRequest
import com.wasin.data.model.user.EmailRequest
import com.wasin.data.model.user.LockConfirmRequest
import com.wasin.data.model.user.LockRequest
import com.wasin.data.model.user.LoginRequest
import com.wasin.data.model.user.LoginResponse
import com.wasin.data.model.user.SignupRequest
import com.wasin.data.util.ApiUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserApi(
    private val client: HttpClient
): UserRepository {
    override suspend fun signup(signupRequest: SignupRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.SIGN_UP.getPath1()) {
            setBody(signupRequest)
        }.body()
    }

    override suspend fun login(loginRequest: LoginRequest): ApiUtils.ApiResult<LoginResponse> {
        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().first().clearToken()
        return client.post(HttpRoutes.LOGIN.getPath1()) {
            setBody(loginRequest)
        }.body()
    }

    override suspend fun logout(): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.LOGOUT.getPath1()).body()
    }

    override suspend fun withdraw(): ApiUtils.ApiResult<String> {
        return client.delete(HttpRoutes.WITHDRAW.getPath1()).body()
    }

    override suspend fun lock(lockRequest: LockRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.LOCK.getPath1()) {
            setBody(lockRequest)
        }.body()
    }

    override suspend fun lockConfirm(lockConfirmRequest: LockConfirmRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.LOCK_CONFIRM.getPath1()) {
            setBody(lockConfirmRequest)
        }.body()
    }

    override suspend fun emailSend(emailRequest: EmailRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.EMAIL_CODE_SEND.getPath1()) {
            setBody(emailRequest)
        }.body()
    }

    override suspend fun emailCheck(emailCheckRequest: EmailCheckRequest): ApiUtils.ApiResult<String> {
        return client.post(HttpRoutes.EMAIL_CODE_CONFIRM.getPath1()) {
            setBody(emailCheckRequest)
        }.body()
    }
}
