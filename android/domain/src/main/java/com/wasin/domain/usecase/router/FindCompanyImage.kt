package com.wasin.domain.usecase.router

import com.wasin.data.data_api.RouterRepository
import com.wasin.data.model.router.FindCompanyImageResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindCompanyImage @Inject constructor(
    private val routerRepository: RouterRepository
) {
    operator fun invoke(): Flow<Resource<FindCompanyImageResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = routerRepository.findCompanyImage()
            val success = result.response ?: FindCompanyImageResponse()
            val errorMessage = result.error?.message ?: "회사 이미지를 불러오는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
