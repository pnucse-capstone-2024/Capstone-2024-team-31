package com.wasin.domain.usecase.backoffice

import com.wasin.data.data_api.BackOfficeRepository
import com.wasin.data.model.backoffice.AcceptRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AcceptAdmin @Inject constructor(
    private val backOfficeRepository: BackOfficeRepository
) {
    operator fun invoke(request: AcceptRequest): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = backOfficeRepository.accept(request)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "관리자를 승인하는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
