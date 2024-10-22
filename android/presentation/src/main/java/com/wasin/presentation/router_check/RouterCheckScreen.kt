package com.wasin.presentation.router_check

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.MyCircularProgress
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._theme.typography

@Composable
fun RouterCheckScreen(
    navController: NavController,
    viewModel: RouterCheckViewModel = hiltViewModel()
) {
    val state = viewModel.result.value
    WithTitle(
        title = "라우터 점검 결과",
        description = "라우터 점검 결과입니다. 문제가 있다면 SSH 접속 이후 ./network_refresh 명령어를 통해 라우터 초기화를 진행해주세요.",
    ) {
        if (state.isLoading) {
            item { MyCircularProgress() }
        }
        else if (state.error.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyEmptyContent(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        message = state.error
                    )
                }
            }
        }
        else {
            item {
                Text(
                    text = state.result,
                    style = typography.bodyLarge
                )
            }
        }
    }
}
