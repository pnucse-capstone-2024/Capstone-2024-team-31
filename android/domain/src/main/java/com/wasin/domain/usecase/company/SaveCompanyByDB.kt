package com.wasin.domain.usecase.company

import com.wasin.data.data_api.CompanyRepository
import com.wasin.data.model.company.SaveCompanyByDBRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveCompanyByDB @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    operator fun invoke(companyByDBRequest: SaveCompanyByDBRequest): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = companyRepository.saveCompanyByDB(companyByDBRequest)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "회사 정보를 저장하는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
