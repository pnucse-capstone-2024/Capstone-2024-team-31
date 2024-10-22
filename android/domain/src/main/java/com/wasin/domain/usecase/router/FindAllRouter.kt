package com.wasin.domain.usecase.router

import com.wasin.data.data_api.RouterRepository
import com.wasin.data.model.router.FindAllRouterResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindAllRouter @Inject constructor(
    private val routerRepository: RouterRepository
) {
    operator fun invoke(): Flow<Resource<FindAllRouterResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = routerRepository.findAll()
            val success = result.response ?: FindAllRouterResponse()
            val errorMessage = result.error?.message ?: "라우터 목록을 불러오는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
