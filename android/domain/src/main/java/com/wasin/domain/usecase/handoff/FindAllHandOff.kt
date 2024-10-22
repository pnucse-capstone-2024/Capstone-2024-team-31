package com.wasin.domain.usecase.handoff

import com.wasin.data.data_api.HandOffRepository
import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.model.handoff.FindAllHandOffResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindAllHandOff @Inject constructor(
    private val handOffRepository: HandOffRepository
) {
    operator fun invoke(request: FindAllHandOffRequest): Flow<Resource<FindAllHandOffResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = handOffRepository.findAll(request)
            val response = result.response ?: FindAllHandOffResponse()
            val errorMessage = result.error?.message ?: "핸드오프 전체 목록을 불러오는데 실패하였습니다."

            if (result.success) emit(Resource.Success(response))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
