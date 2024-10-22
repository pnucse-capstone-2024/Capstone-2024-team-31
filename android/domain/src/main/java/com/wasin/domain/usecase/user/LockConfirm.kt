package com.wasin.domain.usecase.user

import com.wasin.data.data_api.UserRepository
import com.wasin.data.model.user.LockConfirmRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LockConfirm @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(lockConfirmRequest: LockConfirmRequest): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = userRepository.lockConfirm(lockConfirmRequest)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "잠금해제 패스워드 확인에 실패했습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
