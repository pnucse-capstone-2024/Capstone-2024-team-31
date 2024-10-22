package com.wasin.presentation.router_add

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.data.model.router.RouterPosition
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.CompanyImageItem
import com.wasin.presentation._common.ImageMarker
import com.wasin.presentation._common.TextFieldWithTitle
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent

@Composable
fun RouterAddScreen(
    navController: NavController,
    viewModel: RouterAddViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = {  navController.navigate(WasinScreen.RouterListScreen.route) }
    )
    WithTitle("라우터 추가") {
        item {
            TextFieldWithTitle(
                title = "이름(별칭)",
                placeholder = "ex) 휴게실 Wifi",
                text = viewModel.routerDTO.value.name,
                onValueChange = { viewModel.onEvent(RouterAddEvent.EnterName(it)) }
            )
        }
        item {
            TextFieldWithTitle(
                title = "MAC 주소",
                placeholder = "ex) 58:86:94:7f:b4:c4",
                text = viewModel.routerDTO.value.macAddress,
                onValueChange = { viewModel.onEvent(RouterAddEvent.EnterMacAddress(it)) }
            )
        }
        item {
            TextFieldWithTitle(
                title = "시리얼 넘버",
                placeholder = "ex) IPT-98765432",
                text = viewModel.routerDTO.value.serialNumber,
                onValueChange = { viewModel.onEvent(RouterAddEvent.EnterSerialNumber(it)) }
            )
        }
        item {
            TextFieldWithTitle(
                title = "패스워드",
                placeholder = "라우터의 패스워드를 입력해주세요.",
                text = viewModel.routerDTO.value.password,
                onValueChange = { viewModel.onEvent(RouterAddEvent.EnterPassword(it)) }
            )
        }
        item {
            TextFieldWithTitle(
                title = "포트번호",
                placeholder = "ex) 1234",
                text = viewModel.routerDTO.value.port,
                onValueChange = { viewModel.onEvent(RouterAddEvent.EnterPort(it)) }
            )
        }
        item {
            RouterPositionInImage(
                image = viewModel.imageDTO.value.companyImage,
                position = viewModel.position.value,
                onPositionEnter = { x, y -> viewModel.onEvent(RouterAddEvent.EnterPosition(x, y)) },
                onImageSizeEnter = { viewModel.onEvent(RouterAddEvent.EnterImageSize(it)) }
            )
        }
        item {
            BlueLongButton(text = "등록하기") {
                viewModel.onEvent(RouterAddEvent.SaveRouter)
            }
        }
    }
}

@Composable
fun RouterPositionInImage(
    image: String,
    position: RouterPosition?,
    onPositionEnter: (Float, Float) -> Unit = { a, b -> },
    onImageSizeEnter: (Int) -> Unit,
) {
    var localWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(true) {
        onImageSizeEnter(localWidth)
    }

    Column {
        Text(
            text = "위치",
            style = typography.titleMedium,
            modifier = Modifier.padding(bottom = 11.dp)
        )
        Text(
            text = "라우터가 설치된 위치를 클릭해주세요.",
            style = typography.bodyLarge,
            modifier = Modifier.padding(bottom = 11.dp),
            color = gray_808080
        )
        Box (
            modifier = Modifier.fillMaxWidth()
        ) {
            CompanyImageItem(
                modifier = Modifier
                    .onGloballyPositioned {
                        localWidth = it.size.width
                    }
                    .pointerInput(Unit){
                        detectTapGestures { offset ->
                            onPositionEnter(offset.x, offset.y)
                        }
                    }
                ,
                imageUrl = image,
            )
            if (position != null) {
                ImageMarker(
                    Modifier.absoluteOffset {
                        IntOffset(position.x.toInt(), position.y.toInt())
                    }
                )
            }
        }
    }
}
