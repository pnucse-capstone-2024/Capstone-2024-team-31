package com.wasin.presentation.router_update

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.TextFieldWithTitle
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation.router_add.RouterPositionInImage

@Composable
fun RouterUpdateScreen(
    navController: NavController,
    viewModel: RouterUpdateViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = { navController.navigate(WasinScreen.RouterListScreen.route) }
    )
    WithTitle("라우터 수정") {
        item {
            TextFieldWithTitle(
                title = "이름(별칭)",
                placeholder = "휴게실 Wifi",
                text = viewModel.requestDTO.value.name,
                onValueChange = { viewModel.onEvent(RouterUpdateEvent.EnterName(it) )}
            )
        }
        item {
            TextFieldWithTitle(
                title = "패스워드",
                placeholder = "라우터의 패스워드를 입력해주세요.",
                text = viewModel.requestDTO.value.password,
                onValueChange = { viewModel.onEvent(RouterUpdateEvent.EnterPassword(it)) }
            )
        }
        item {
            RouterPositionInImage(
                image = viewModel.routerDTO.value.image.companyImage,
                position = viewModel.position.value,
                onPositionEnter = { a, b -> viewModel.onEvent(RouterUpdateEvent.EnterPosition(a, b)) },
                onImageSizeEnter = { viewModel.onEvent(RouterUpdateEvent.EnterImageSize(it)) },
            )
        }
        item {
            BlueLongButton(text = "수정하기") {
                viewModel.onEvent(RouterUpdateEvent.UpdateRouter)
            }
        }
    }
}
