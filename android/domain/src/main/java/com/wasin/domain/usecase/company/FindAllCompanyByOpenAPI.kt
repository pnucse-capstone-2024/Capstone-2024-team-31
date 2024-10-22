package com.wasin.domain.usecase.company

import com.wasin.data.data_api.CompanyRepository
import com.wasin.data.model.company.FindAllCompanyByOpenAPIRequest
import com.wasin.data.model.company.FindAllCompanyByOpenAPIResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindAllCompanyByOpenAPI @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    operator fun invoke(request: FindAllCompanyByOpenAPIRequest): Flow<Resource<FindAllCompanyByOpenAPIResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = companyRepository.findAllCompanyByOpenAPI(request)
            val success = result.response ?: FindAllCompanyByOpenAPIResponse(emptyList())
            val errorMessage = result.error?.message ?: "회사 정보를 불러오는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
