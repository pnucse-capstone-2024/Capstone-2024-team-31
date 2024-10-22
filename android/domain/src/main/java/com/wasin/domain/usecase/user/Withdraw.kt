package com.wasin.domain.usecase.user

import com.wasin.data._const.DataStoreKey
import com.wasin.data.data_api.UserRepository
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Withdraw @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStore: WasinDataStore
) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = userRepository.withdraw()
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "회원탈퇴에 실패했습니다."

            if (result.success) {
                setDataForInit()
                emit(Resource.Success(success))
            }
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }

    private fun setDataForInit() {
        dataStore.clear(DataStoreKey.EMAIL_KEY.name)
        dataStore.clear(DataStoreKey.ACCESS_TOKEN_KEY.name)
        dataStore.clear(DataStoreKey.REFRESH_TOKEN_KEY.name)
    }
}
