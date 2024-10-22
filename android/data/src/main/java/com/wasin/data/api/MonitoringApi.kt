package com.wasin.data.api

import com.wasin.data._const.HttpRoutes
import com.wasin.data.data_api.MonitoringRepository
import com.wasin.data.model.monitoring.FindMonitoringByIdResponse
import com.wasin.data.model.monitoring.FindMultipleMonitorResponse
import com.wasin.data.util.ApiUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MonitoringApi (
    private val client: HttpClient
): MonitoringRepository {
    override suspend fun monitorById(
        metricId: Long?,
        routerId: Int,
        time: Int?
    ): ApiUtils.ApiResult<FindMonitoringByIdResponse> {
        var path = HttpRoutes.MONITORING_BY_ID.getPath1() + "/$routerId"
        val sign = if (metricId == null) "?" else "&"
        if (metricId != null) path += "?metricId=$metricId"
        if (time != null) path += "${sign}time=$time"
        return client.get(path).body()
    }

    override suspend fun monitorMultiple(
        metricId: Long?,
        time: Int?
    ): ApiUtils.ApiResult<FindMultipleMonitorResponse> {
        var path = HttpRoutes.MONITORING_MULTIPLE.getPath1()
        val sign = if (metricId == null) "?" else "&"
        if (metricId != null) path += "?metricId=$metricId"
        if (time != null) path += "${sign}time=$time"
        return client.get(path).body()
    }

}
