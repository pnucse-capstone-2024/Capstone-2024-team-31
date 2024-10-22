package com.wasin.data.data_api

import com.wasin.data.model.monitoring.FindMonitoringByIdResponse
import com.wasin.data.model.monitoring.FindMultipleMonitorResponse
import com.wasin.data.util.ApiUtils

interface MonitoringRepository {
    suspend fun monitorById(metricId: Long?, routerId: Int, time: Int?): ApiUtils.ApiResult<FindMonitoringByIdResponse>

    suspend fun monitorMultiple(metricId: Long?, time: Int?): ApiUtils.ApiResult<FindMultipleMonitorResponse>
}
