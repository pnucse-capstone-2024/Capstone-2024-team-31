package com.wasin.domain.usecase.handoff

import com.wasin.data.data_api.HandOffRepository
import com.wasin.data.model.handoff.FindBestHandOffRequest
import com.wasin.data.model.handoff.FindBestHandOffResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindBestHandOff @Inject constructor(
    private val handOffRepository: HandOffRepository
) {
    operator fun invoke(request: FindBestHandOffRequest): Flow<Resource<FindBestHandOffResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = handOffRepository.findBestRouter(request)
            val success = result.response ?: FindBestHandOffResponse()
            val errorMessage = result.error?.message ?: "핸드오프 최적 라우터를 불러오는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
