package com.wasin.domain.usecase.profile

import com.wasin.data.data_api.ProfileRepository
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeProfile @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(profileId: Long): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val result = profileRepository.changeProfile(profileId)
            val success = result.response ?: ""
            val errorMessage = result.error?.message ?: "프로파일 모드를 바꾸는 데 실패하였습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
