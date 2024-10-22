package com.wasin.domain.usecase.company

import com.wasin.data.data_api.CompanyRepository
import com.wasin.data.model.company.SaveCompanyByOpenAPIRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class SaveCompanyByOpenAPI @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    operator fun invoke(
        companyByOpenAPIRequest: SaveCompanyByOpenAPIRequest,
        file: File
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = companyRepository.saveCompanyByOpenAPI(companyByOpenAPIRequest, file)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "회사 정보를 저장하는데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
