package com.wasin.domain.usecase.monitoring

import com.wasin.data.data_api.MonitoringRepository
import com.wasin.data.model.monitoring.FindMonitoringByIdResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindMonitoringByRouterId @Inject constructor(
    private val monitoringRepository: MonitoringRepository
) {
    operator fun invoke(metricId: Long?, routerId: Int, time: Int?): Flow<Resource<FindMonitoringByIdResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = monitoringRepository.monitorById(metricId, routerId, time)
            val success = result.response ?: FindMonitoringByIdResponse()
            val errorMessage = result.error?.message ?: "라우터 개별 모니터링에 실패했습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
