package com.wasin.presentation.monitoring

import com.wasin.data.model.monitoring.FindMultipleMonitorResponse

data class MonitoringMultipleState (
    val isLoading: Boolean = true,
    val metrics: FindMultipleMonitorResponse = FindMultipleMonitorResponse(),
    val errorMessage: String = ""
)
