package com.wasin.domain.usecase.handoff

import com.wasin.data.data_api.HandOffRepository
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeHandOffModeAuto @Inject constructor(
    private val handOffRepository: HandOffRepository
) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = handOffRepository.changeHandOffModeAuto()
            val success = result.response ?: "성공"
            val errorMessage = result.error?.message ?: "핸드오프 모드를 자동으로 변경하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
