package com.wasin.domain.usecase.user

import com.wasin.data._const.DataStoreKey
import com.wasin.data.data_api.UserRepository
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.user.LoginRequest
import com.wasin.data.model.user.LoginResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Login @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStore: WasinDataStore
) {
    operator fun invoke(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = userRepository.login(loginRequest)
            val success = result.response ?: LoginResponse("", "")
            val errorMessage = result.error?.message ?: "로그인에 실패하였습니다."

            if (result.success) {
                dataStore.setData(DataStoreKey.ACCESS_TOKEN_KEY.name, success.accessToken)
                dataStore.setData(DataStoreKey.REFRESH_TOKEN_KEY.name, success.refreshToken)
                emit(Resource.Success(success))
            }
            else {
                emit(Resource.Error(errorMessage))
            }
        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
