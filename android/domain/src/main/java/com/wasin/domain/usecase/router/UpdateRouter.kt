package com.wasin.domain.usecase.router

import com.wasin.data.data_api.RouterRepository
import com.wasin.data.model.router.UpdateRouterRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateRouter @Inject constructor(
    private val routerRepository: RouterRepository
) {
    operator fun invoke(request: UpdateRouterRequest, routerId: Long): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = routerRepository.update(request, routerId)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "라우터를 수정하는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
