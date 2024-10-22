package com.wasin.domain.usecase.monitoring

import com.wasin.data.data_api.MonitoringRepository
import com.wasin.data.model.monitoring.FindMultipleMonitorResponse
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindMonitorMultiple @Inject constructor(
    private val monitoringRepository: MonitoringRepository
) {
    operator fun invoke(metricId: Long?, time: Int?): Flow<Resource<FindMultipleMonitorResponse>> = flow {
        try {
            emit(Resource.Loading())
            val result = monitoringRepository.monitorMultiple(metricId, time)
            val success = result.response ?: FindMultipleMonitorResponse()
            val errorMessage = result.error?.message ?: "모니터링 다중 라우터 조회에 실패했습니다."

            if (result.success) emit(Resource.Success(success))
            else emit(Resource.Error(errorMessage))

        } catch(error: Exception){
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
