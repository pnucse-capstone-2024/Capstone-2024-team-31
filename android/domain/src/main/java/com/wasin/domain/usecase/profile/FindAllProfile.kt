package com.wasin.domain.usecase.profile

import com.wasin.data.data_api.ProfileRepository
import com.wasin.data.model.profile.FindAllProfileResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindAllProfile @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Resource<FindAllProfileResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = profileRepository.findAllProfile()
            val success = result.response ?: FindAllProfileResponse()
            val errorMessage = result.error?.message ?: "프로파일 목록을 불러오는 데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
