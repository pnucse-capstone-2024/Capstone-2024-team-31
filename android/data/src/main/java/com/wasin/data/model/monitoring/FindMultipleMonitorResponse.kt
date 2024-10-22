package com.wasin.data.model.monitoring

import kotlinx.serialization.Serializable

@Serializable
data class FindMultipleMonitorResponse(
    val activeMetricId: Long = 0,
    val settingTime: Long = 0,
    val metricList: List<MonitoringMetric> = emptyList(),
    val graphList: List<MonitoringGraph> = emptyList()
) {
    @Serializable
    data class MonitoringMetric(
        val metric: String,
        val metricId: Long
    )

    @Serializable
    data class MonitoringGraph(
        val labels: String,
        val timeList: List<Double>,
        val valueList: List<Double>
    )

}
