package com.wasin.data.repository

import com.wasin.data.api.MonitoringApi
import com.wasin.data.data_api.MonitoringRepository
import com.wasin.data.model.monitoring.FindMonitoringByIdResponse
import com.wasin.data.model.monitoring.FindMultipleMonitorResponse
import com.wasin.data.util.ApiUtils
import javax.inject.Inject

class MonitoringRepositoryImpl @Inject constructor(
    private val monitoringApi: MonitoringApi
): MonitoringRepository {
    override suspend fun monitorById(
        metricId: Long?,
        routerId: Int,
        time: Int?
    ): ApiUtils.ApiResult<FindMonitoringByIdResponse> {
        return monitoringApi.monitorById(metricId, routerId, time)
    }

    override suspend fun monitorMultiple(metricId: Long?, time: Int?): ApiUtils.ApiResult<FindMultipleMonitorResponse> {
        return monitoringApi.monitorMultiple(metricId, time)
    }
}
