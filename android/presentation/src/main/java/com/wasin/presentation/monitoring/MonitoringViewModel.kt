package com.wasin.presentation.monitoring

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.monitoring.FindMultipleMonitorResponse
import com.wasin.domain.usecase.monitoring.FindMonitorMultiple
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
    private val findMonitorMultiple: FindMonitorMultiple,
): ViewModel() {

    val selectedTabIndex = mutableIntStateOf(0)
    val activeTime = mutableStateOf(TimeEnum.ONE_HOUR_AGO)
    private val metricId = mutableStateOf<Long?>(null)

    private val _monitoring = mutableStateOf(MonitoringMultipleState())
    val monitoring = _monitoring

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        findMonitoring()
    }

    fun onTabClick(tabIndex: Int, _metricId: Long?) {
        if (selectedTabIndex.intValue != tabIndex){
            selectedTabIndex.intValue = tabIndex
            metricId.value = _metricId
            findMonitoring()
        }
    }

    fun refreshTime(index: Int) {
        activeTime.value = TimeEnum.entries.getOrNull(index) ?: TimeEnum.THIRTY_MINUTES_AGO
        findMonitoring()
    }

    fun refresh() {
        findMonitoring()
    }

    private fun findMonitoring() {
        findMonitorMultiple(
            metricId.value,
            activeTime.value.time
        ).onEach { response ->
            _monitoring.value = _monitoring.value.copy(
                metrics = response.data ?: FindMultipleMonitorResponse(),
                isLoading = response is Resource.Loading,
                errorMessage = if (response is Resource.Error) response.message else ""
            )
        }.launchIn(viewModelScope)
    }

}
