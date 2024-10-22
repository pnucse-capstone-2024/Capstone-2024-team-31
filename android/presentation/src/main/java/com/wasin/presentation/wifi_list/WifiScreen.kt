package com.wasin.presentation.wifi_list

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.data.model.handoff.FindAllHandOffResponse
import com.wasin.presentation._common.FilterDropDownButton
import com.wasin.presentation._common.GrayDivider
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.ShortButton
import com.wasin.presentation._common.TextFieldWithTitle
import com.wasin.presentation._common.WithTitleAndRefresh
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.getWifi

@Composable
fun WifiScreen(
    navController: NavController,
    viewModel: WifiViewModel = hiltViewModel(),
) {
    LaunchedEffectEvent(viewModel.eventFlow)
    WithTitleAndRefresh(
        onRefresh = { viewModel.getWifiList() },
        title = "Wifi 연결",
        description = "자동 또는 수동으로 최적의 와이파이에 연결되도록 설정할 수 있어요.",
        containSetting = true,
        onSettingClick = { navController.navigate(WasinScreen.SettingScreen.route) }
    ) {
        item {
            WifiAutoMode(
                isAuto = viewModel.wifiList.value.isAuto,
                changeModeManual = { viewModel.changeHandOffModeManual() },
                changeModeAuto = { viewModel.changeHandOffModeAuto() }
            )
        }
        item { GrayDivider() }
        item {
            WifiListComponent(
                currentSSID = viewModel.currentSSID.value,
                sortText = viewModel.wifiSort.value.kor,
                password = { viewModel.getPassword(it) },
                wifiList = viewModel.wifiList.value.routerList,
                openWifiSetting = { viewModel.openWifiSettings() },
                connectWifi = { a, b -> viewModel.connectInReal(a, b) },
                onSort = { viewModel.sort(it) }
            )
        }
    }
}

@Composable
fun WifiListComponent(
    currentSSID: String = "",
    sortText: String,
    password: (String) -> String,
    wifiList: List<FindAllHandOffResponse.RouterWithStateDTO>,
    openWifiSetting: () -> Unit,
    connectWifi: (String, String) -> Unit,
    onSort: (Int) -> Unit = {},
) {
    Column {
        WifiListTitle()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "정렬 기준",
                style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            FilterDropDownButton(
                text = sortText,
                modifier = Modifier.align(Alignment.CenterEnd),
                selectList = WifiSort.entries.map { it.kor },
                onClick = onSort
            )
        }
        if (wifiList.isEmpty()) {
            MyEmptyContent()
        }
        else {
            wifiList.forEach {
                WifiItemComponent(
                    ssid = it.ssid,
                    level = it.level.toInt(),
                    score = it.score.toInt(),
                    password = it.password.ifEmpty { password(it.ssid) },
                    isSystemExist = it.isSystemExist,
                    isCurrentConnect = it.ssid == currentSSID,
                    connectWifi = connectWifi,
                    openWifiSetting = openWifiSetting
                )
            }
        }
    }
}

@Composable
fun WifiItemComponent(
    ssid: String = "휴게실 와이파이",
    level: Int = 0,
    score: Int = 0,
    password: String = "",
    isSystemExist: Boolean,
    isCurrentConnect: Boolean = false,
    connectWifi: (String, String) -> Unit,
    openWifiSetting: () -> Unit,
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    if (isDialogOpen.value) {
        WifiPasswordDialog(
            ssid = ssid,
            connectWifi = connectWifi,
            password = password,
            onDismissRequest = { isDialogOpen.value = false }
        )
    }
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                WifiWithIcon(ssid, level, score, isSystemExist)
            }
            ShortButton(
                text = "연결",
                isSelected = !isCurrentConnect,
                onClick = {
                    if (isCurrentConnect) {
                        // do nothing
                    }
                    else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        isDialogOpen.value = true
                    }
                    else openWifiSetting()
                },
            )
        }
        GrayDivider(modifier = Modifier.padding(top = 17.dp))
    }
}

@Composable
fun WifiPasswordDialog(
    ssid: String = "",
    password: String = "",
    connectWifi: (String, String) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    val text = remember { mutableStateOf(password) }
    Dialog(onDismissRequest) {
        Column (
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(30.dp)
        ) {
            Text(
                text = ssid,
                style = typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 45.dp)
            )
            TextFieldWithTitle(
                title = "비밀번호",
                text = text.value,
                placeholder = "비밀번호를 입력해주세요",
                onValueChange = { text.value = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            ShortButton(
                text = "연결",
                isSelected = true,
                onClick = {
                    connectWifi(ssid, text.value)
                    onDismissRequest()
                },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun WifiWithIcon(name: String, level: Int, score: Int, isSystemExist: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = getWifi(level, isSystemExist)),
            contentDescription = "wifi level",
            modifier = Modifier
                .padding(end = 15.dp)
                .width(30.dp)
        )
        Column {
            Text(
                text = name,
                style = typography.titleMedium,
                modifier = Modifier.padding(bottom = 7.dp)
            )
            Text(
                text = "상태: ${score}점",
                style = typography.bodyMedium,
                color = gray_808080
            )
        }
    }
}

@Composable
private fun WifiListTitle() {
    Text(
        text = "Wifi 목록",
        style = typography.titleMedium,
        modifier = Modifier.padding(bottom = 13.dp)
    )
    Text(
        text = "네트워크 품질을 확인할 수 있는 Wifi는 분홍색으로, 정확히 알 수 없는 Wifi는 파란색으로 표시돼요.",
        style = typography.bodyMedium
    )
}

@Composable
fun WifiAutoMode(
    isAuto: Boolean,
    changeModeManual: () -> Unit,
    changeModeAuto: () -> Unit,
) {
    Column {
        Text(
            text = "모드",
            style = typography.titleMedium,
            modifier = Modifier.padding(bottom = 13.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShortButton(
                text = "자동",
                onClick = changeModeAuto,
                isSelected = isAuto
            )
            ShortButton(
                text = "수동",
                onClick = changeModeManual,
                isSelected = !isAuto
            )
        }
    }
}
