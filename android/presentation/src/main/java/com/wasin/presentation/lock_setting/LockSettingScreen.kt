package com.wasin.presentation.lock_setting

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.LockComponent
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.WasinBackHandler

@Composable
fun LockSettingScreen(
    navController: NavController,
    viewModel: LockSettingViewModel = hiltViewModel(),
) {
    WasinBackHandler()
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = {
            val nextScreen = WasinScreen.LockConfirmScreen.route
            viewModel.saveScreenState(nextScreen)
            navController.navigate(nextScreen){
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    )
    WithTitle(
        title = "잠금 해제 비밀번호 입력",
        description = "높은 보안성을 유지하기 위해 잠금 해제시 사용할 비밀번호를 입력해주세요."
    ) {
        item {
            LockComponent(
                password = viewModel.lockDTO.value,
                onAdd = { viewModel.addPassword(it) },
                onDelete = { viewModel.removeLast() },
            )
        }
    }
}
