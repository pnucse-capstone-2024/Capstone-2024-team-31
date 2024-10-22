package com.wasin.domain.usecase.user

import com.wasin.data.data_api.UserRepository
import com.wasin.data.model.user.EmailCheckRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmailCheck @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(emailCheckRequest: EmailCheckRequest): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = userRepository.emailCheck(emailCheckRequest)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "이메일 코드 확인 실패했습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
