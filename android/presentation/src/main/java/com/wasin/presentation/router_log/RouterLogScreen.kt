package com.wasin.presentation.router_log

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.MyCircularProgress
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._theme.typography

@Composable
fun RouterLogScreen(
    navController: NavController,
    viewModel: RouterLogViewModel = hiltViewModel()
){
    val state = viewModel.result.value
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FloatingButton{ viewModel.sendEmail() } },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.White
    ) {
        WithTitle(
            title = "라우터 로그",
            description = "라우터 상태를 출력한 내용입니다.",
        ){
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
                        text = state.log,
                        style = typography.bodyLarge
                    )
                }
            }
            it
        }
    }
}

@Composable
fun FloatingButton(
    onClick: () -> Unit
) {
    BlueLongButton(
        text = "이메일 전송하기",
        onClick = onClick
    )
}
