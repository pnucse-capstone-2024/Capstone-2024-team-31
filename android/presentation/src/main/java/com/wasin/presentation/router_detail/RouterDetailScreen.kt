package com.wasin.presentation.router_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.data.model.router.FindByRouterIdResponse
import com.wasin.presentation._common.CompanyImageItem
import com.wasin.presentation._common.ImageMarker
import com.wasin.presentation._common.WithTitleAndRefresh
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.getScoreColor
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation.setting.SettingContentTheme
import com.wasin.presentation.setting.WithArrowItem
import kotlin.math.roundToInt

@Composable
fun RouterDetailScreen(
    routerId: Long,
    navController: NavController,
    viewModel: RouterDetailViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = { navController.navigate(WasinScreen.RouterListScreen.route) }
    )
    WithTitleAndRefresh(
        title = viewModel.routerDTO.value.information.name,
        onRefresh = { viewModel.refresh() }
    ) {
        item {
            CompanyAndRouter(
                image = viewModel.routerDTO.value.image.companyImage,
                information = viewModel.routerDTO.value.information,
                enterImageSize = { viewModel.enterImageSize(it) }
            )
        }
        item { RouterState(viewModel.routerDTO.value.information) }
        item {
            RouterMonitoring{
                navController.navigate(WasinScreen.MonitoringByRouterScreen.route + "?routerId=$routerId")
            } 
        }
        item {
            SystemRestore(
                onCheck = { navController.navigate(WasinScreen.RouterCheckScreen.route + "?routerId=$routerId") },
                onSend = { navController.navigate(WasinScreen.RouterLogScreen.route + "?routerId=$routerId") }
            )
        }
        item {
            Editing(
                onUpdateClick = { navController.navigate(WasinScreen.RouterUpdateScreen.route + "?routerId=${routerId}") },
                onDeleteClick = { viewModel.deleteRouter() }
            )
        }
    }
}

@Composable
fun CompanyAndRouter(
    image: String,
    information: FindByRouterIdResponse.RouterInformation,
    enterImageSize: (Int) -> Unit
) {
    var localWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(true) {
        enterImageSize(localWidth)
    }

    Box (
        modifier = Modifier.fillMaxWidth()
    ) {
        CompanyImageItem(
            modifier = Modifier
                .onGloballyPositioned {
                    localWidth = it.size.width
                }
            ,
            imageUrl = image
        )
        ImageMarker(
            modifier = Modifier.absoluteOffset {
                IntOffset(information.positionX.roundToInt(), information.positionY.roundToInt())
            },
            color = getScoreColor(information.score),
        )
    }
}

@Composable
fun RouterState(
    information: FindByRouterIdResponse.RouterInformation,
) {
    SettingContentTheme(
        title = "상태 확인"
    ) {
        RouterStateItem("이름", information.name)
        RouterStateItem("SSID", information.ssid)
        RouterStateItem("IP주소", information.instance)
        RouterStateItem("MAC 주소", information.macAddress)
        RouterStateItem("시리얼 넘버", information.serialNumber)
        RouterStateItem("포트번호", information.port)
        RouterStateItem("비밀번호", information.password)
        RouterStateItem("상태", "${information.score}점")
    }
}

@Composable
fun RouterStateItem(
    title: String = "",
    content: String = "",
) {
    Row {
        Text(
            modifier = Modifier.width(100.dp),
            text = title,
            style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = content,
            style = typography.bodyLarge,
        )
    }
}

@Composable
fun RouterMonitoring(
    onClick: () -> Unit,
) {
    SettingContentTheme(
        title = "모니터링"
    ) {
        WithArrowItem(
            text = "공유기 모니터링",
            onClick = onClick
        )
    }
}

@Composable
fun SystemRestore(
    onCheck: () -> Unit,
    onSend: () -> Unit
) {
    SettingContentTheme(
        title = "복구하기"
    ) {
        WithArrowItem("시스템 점검하기", onCheck)
        WithArrowItem("로그 파일 이메일 전송하기", onSend)
    }
}

@Composable
fun Editing(onUpdateClick: () -> Unit, onDeleteClick: () -> Unit) {
    SettingContentTheme(
        title = "편집하기",
        isLast = true
    ) {
        WithArrowItem("수정하기", onUpdateClick)
        WithArrowItem("삭제하기", onDeleteClick)
    }
}
