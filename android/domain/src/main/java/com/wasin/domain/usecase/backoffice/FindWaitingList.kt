package com.wasin.domain.usecase.backoffice

import com.wasin.data.data_api.BackOfficeRepository
import com.wasin.data.model.backoffice.FindWaitingListResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindWaitingList @Inject constructor(
    private val backOfficeRepository: BackOfficeRepository
) {
    operator fun invoke(): Flow<Resource<FindWaitingListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = backOfficeRepository.findWaitingList()
            val success = result.response ?: FindWaitingListResponse(emptyList())
            val errorMessage = result.error?.message ?: "승인 대기중인 관리자를 불러오는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
