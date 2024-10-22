package com.wasin.presentation.backoffice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.ShortButton
import com.wasin.presentation._common.WithTitleAndRefresh
import com.wasin.presentation._theme.gray_E8E8E8
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent

@Composable
fun BackOfficeScreen(
    navController: NavController,
    viewModel: BackOfficeViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(viewModel.eventFlow)
    WithTitleAndRefresh(
        onRefresh = { viewModel.findWaitingList() },
        title = "관리자 승인 대기"
    ) {
        val waitingList = viewModel.waitingList.value.waitingList
        if (waitingList.isEmpty()) {
            item { MyEmptyContent() }
        }
        else {
            items(waitingList) { wait ->
                WaitingItem(
                    name = wait.name,
                    onClick = {
                        viewModel.acceptAdmin(wait.userId, wait.name)
                    }
                )
            }
        }
    }
}

@Composable
fun WaitingItem(
    name: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(BorderStroke(0.5.dp, gray_E8E8E8), RoundedCornerShape(30.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = typography.titleMedium,
        )
        ShortButton(
            text = "승인",
            isSelected = true,
            onClick = onClick
        )
    }
}
