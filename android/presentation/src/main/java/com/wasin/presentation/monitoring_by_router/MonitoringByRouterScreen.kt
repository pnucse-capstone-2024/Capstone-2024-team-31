package com.wasin.presentation.monitoring_by_router

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.FilterDropDownButton
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation.monitoring.MonitoringTabLayer
import com.wasin.presentation.monitoring.TimeEnum

@Composable
fun MonitoringByRouterScreen(
    navController: NavController,
    viewModel: MonitoringByRouterViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(viewModel.eventFlow)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FilterDropDownButton(
            modifier = Modifier.align(Alignment.End).padding(top = 10.dp, start = 7.dp),
            text = viewModel.activeTime.value.kor,
            selectList = TimeEnum.entries.map { it.kor }.toList(),
            onClick = { viewModel.refreshTime(it) }
        )
        MonitoringTabLayer(
            state = viewModel.monitoring.value,
            selectedTabIndex = viewModel.selectedTabIndex.intValue,
            onTabClick = { tabIndex, metricId ->
                viewModel.onTabClick(tabIndex, metricId)
            }
        )
    }
}
